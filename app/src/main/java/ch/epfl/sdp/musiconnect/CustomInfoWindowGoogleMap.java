package ch.epfl.sdp.musiconnect;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.musiconnect.events.Event;

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
        TextView details_tv = view.findViewById(R.id.details);


        name_tv.setText(marker.getTitle());
        String type = marker.getSnippet();
        switch(type) {
            case "Musician":
                Musician m = (Musician) marker.getTag();
                Map<Instrument, Level> instruments = m.getInstruments();
                String instrList = "No instruments listed";
                if (!instruments.isEmpty()) {
                    StringBuilder list = new StringBuilder();
                    ArrayList<Instrument> listOfKey = new ArrayList<>(instruments.keySet());
                    for (int i = 0; i < listOfKey.size() && i < 5; i++) {
                        list.append(listOfKey.get(i).toString() + " : " + instruments.get(listOfKey.get(i)).toString() + "\n");
                    }
                    instrList = list.toString();
                }
                details_tv.setText(instrList);
                break;
            case "Event":
                Event e = (Event) marker.getTag();
                String descr = e.getDescription();
                String date = e.getDateTime().toString();
                String details = descr + "\n" + date;
                details_tv.setText(details);
                break;
            default:
        }
        return view;
    }

}
