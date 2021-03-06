package ch.epfl.sdp.musiconnect.pages;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.Instrument;
import ch.epfl.sdp.musiconnect.Level;
import ch.epfl.sdp.musiconnect.users.Musician;
import ch.epfl.sdp.musiconnect.users.User;
import ch.epfl.sdp.musiconnect.cloud.CloudCallback;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageSingleton;
import ch.epfl.sdp.musiconnect.database.DbAdapter;
import ch.epfl.sdp.musiconnect.database.DbCallback;
import ch.epfl.sdp.musiconnect.database.DbDataType;
import ch.epfl.sdp.musiconnect.roomdatabase.AppDatabase;
import ch.epfl.sdp.musiconnect.roomdatabase.MusicianDao;

import static ch.epfl.sdp.musiconnect.functionnalities.ConnectionCheck.checkConnection;

public abstract class ProfilePage extends Page {
    protected TextView titleView, firstNameView, lastNameView, usernameView, emailView, birthdayView;
    protected TextView instrument;
    protected TextView selectedInstrument;
    protected TextView level;
    protected TextView selectedLevel;
    protected Uri videoUri = null;
    protected VideoView mVideoView;
    protected ImageView imgVw;
    protected String userEmail;
    protected DbAdapter dbAdapter;
    protected Musician currentCachedUser;

    protected void loadProfileContent() {
        if (userEmail == null || userEmail.isEmpty()) {
            loadNullProfile();
            return;
        }

        Executor mExecutor = Executors.newSingleThreadExecutor();
        AppDatabase localDb = AppDatabase.getInstance(this);
        MusicianDao mdao = localDb.musicianDao();

        //fetches the current user's profile
        mExecutor.execute(() -> {
            List<Musician> result = mdao.loadAllByIds(new String[]{userEmail});
            currentCachedUser = result.isEmpty() ? null : result.get(0);
        });

        try { // wait for async thread to fetch cached profile
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // gets profile info from database
        if (checkConnection(ProfilePage.this)) {
            dbAdapter.read(DbDataType.Musician, userEmail, new DbCallback() {
                @Override
                public void readCallback(User user) {
                    loadUserProfile(user);
                    // if user profile isn't cached,cache it
                    if (currentCachedUser == null || !ProfileModificationPage.changeStaged) {
                        mExecutor.execute(() -> {
                            mdao.insertAll((Musician) user);
                        });
                    }
                }

                @Override
                public void readFailCallback() {
                    loadNullProfile();
                }
            });

        } else {
            if (currentCachedUser == null) {
                Toast.makeText(this, R.string.unable_to_fetch_info_no_internet, Toast.LENGTH_LONG).show();
            } else { // set profile info based on cache
                loadUserProfile(currentCachedUser);
            }
        }
    }

    protected abstract void loadUserProfile(User user);

    protected void loadInstrument(User user) {
        Musician m = (Musician) user;
        if (!m.getInstruments().isEmpty()) {
            Instrument instr = (Instrument) m.getInstruments().keySet().toArray()[0];
            String i = instr.toString().substring(0, 1).toUpperCase() + instr.toString().substring(1);
            selectedInstrument.setText(i);

            Level lvl = m.getInstruments().get(instr);
            String l = lvl.toString().substring(0, 1).toUpperCase() + lvl.toString().substring(1);
            selectedLevel.setText(l);
        }
    }

    private void loadNullProfile() {
        setContentView(R.layout.activity_visitor_profile_page_null);
    }

    protected void showVideo(Uri uri) {
        if (uri != null) {
            mVideoView.setVideoURI(uri);
            mVideoView.start();
            mVideoView.setOnCompletionListener(mediaPlayer -> mVideoView.start());
        }
    }

    protected void getVideoUri(String s) {
        CloudStorage storage = CloudStorageSingleton.getCloudInstance(this);
        storage.download(CloudStorage.FileType.video, s, new CloudCallback() {
            @Override
            public void onSuccess(Uri fileUri) {
                videoUri = fileUri;
                showVideo(videoUri);
            }

            @Override
            public void onFailure() {
                videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.minion);
                showVideo(videoUri);
            }
        });
    }
}
