package ch.epfl.sdp.musiconnect.roomdatabase;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.musiconnect.Instrument;
import ch.epfl.sdp.musiconnect.Level;

public class InstrumentConverter {
    @TypeConverter
    public static String toStringList(Map<Instrument, Level> instruments){
        if(instruments.isEmpty())
            return "";
        ArrayList<String> strInstr = new ArrayList<>();
        for(Map.Entry<Instrument, Level> entry:instruments.entrySet()){
            strInstr.add(entry.getKey().toString() + "/" + entry.getValue().toString());
        }
        return TextUtils.join("&",strInstr);
    }

    @TypeConverter
    public static Map<Instrument, Level> toInstrumentMap(String instruments){
        if(instruments == "")
            return new HashMap<Instrument, Level>();
        Map<Instrument, Level> mapInstr = new HashMap<Instrument, Level>();
        String[] strInstr = instruments.split("&");
        for(String s:strInstr){
            String[] entry = s.split("/");
            if(entry.length != 2){
                return new HashMap<Instrument, Level>();
            }
            if(entry[0].length() == 0 || entry[1].length() == 0){
                return new HashMap<Instrument, Level>();
            }
            mapInstr.put(Instrument.valueOf(entry[0]),Level.valueOf(entry[1]));
        }
        return mapInstr;
    }
}
