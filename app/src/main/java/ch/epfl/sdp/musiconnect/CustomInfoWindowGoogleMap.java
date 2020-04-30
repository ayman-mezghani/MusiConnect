package ch.epfl.sdp.musiconnect;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.cloud.CloudCallback;
import ch.epfl.sdp.musiconnect.cloud.CloudStorage;
import ch.epfl.sdp.musiconnect.cloud.CloudStorageGenerator;
import ch.epfl.sdp.musiconnect.cloud.FirebaseCloudStorage;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);

        TextView name_tv = view.findViewById(R.id.username);
        VideoView vid = view.findViewById(R.id.vid);
        TextView details_tv = view.findViewById(R.id.details);


        name_tv.setText(marker.getTitle());
        Musician m = (Musician) marker.getTag();
        getVideoUri(vid,m.getEmailAddress());

        return view;
    }

    private void showVideo(VideoView v, Uri videoUri) {
        if (videoUri != null) {
            v.setVideoURI(videoUri);
            v.start();
            v.setOnCompletionListener(mediaPlayer -> v.start());
        }
    }

    private void getVideoUri(VideoView v,String s) {
        CloudStorage storage = CloudStorageGenerator.getCloudInstance(context);
        String path = s + "/" + FirebaseCloudStorage.FileType.video;
        String saveName = s + "_" + FirebaseCloudStorage.FileType.video;
        try {
            storage.download(path, saveName, new CloudCallback() {
                Uri videoUri;
                @Override
                public void onSuccess(Uri fileUri) {
                    videoUri = fileUri;
                    showVideo(v,videoUri);
                }

                @Override
                public void onFailure() {
                    videoUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.minion);
                    showVideo(v,videoUri);
                }
            });
        } catch (IOException e) {
            Toast.makeText(context, "An error occured, please contact support.", Toast.LENGTH_LONG).show();
        }
    }
}
