package ca.georgebrown.comp3074.comp3074_project;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class FetchAddressTask extends AsyncTask<Location, Void, String> {

    private final String TAG = FetchAddressTask.class.getSimpleName();
    private Context context;
    private OnTaskCompleted listener;

    interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }

    FetchAddressTask(Context context, OnTaskCompleted listener){
        this.context = context;
        this.listener = listener;
    }
    @Override
    protected String doInBackground(Location... locations) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Location location = locations[0];
        List<Address> addresses = null;
        String resultMessage = "";
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address
                    1);
            if (addresses == null || addresses.size() == 0) {
                if (resultMessage.isEmpty()) {
                    resultMessage = "No address found";
                    Log.e(TAG, resultMessage);
                }
            }else {
                // If an address is found, read it into resultMessage
                Address address = addresses.get(0);
                ArrayList<String> addressParts = new ArrayList<>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressParts.add(address.getAddressLine(i));
                }

                resultMessage = TextUtils.join("\n", addressParts);
            }
        }catch (IOException ioException) {
            // Catch network or other I/O problems
            resultMessage = "Service not available";
            Log.e(TAG, resultMessage, ioException);
        }catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values
            resultMessage = "Invalid lat long used";
            Log.e(TAG, resultMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }
        return resultMessage;    }

    @Override
    protected void onPostExecute(String s) {
        listener.onTaskCompleted(s);
        super.onPostExecute(s);    }
}
