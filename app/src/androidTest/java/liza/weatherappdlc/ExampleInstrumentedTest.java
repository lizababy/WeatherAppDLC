package liza.weatherappdlc;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("liza.weatherappdlc", appContext.getPackageName());
        assertEquals("liza.weatherappdlc", mActivityRule.getActivity().getPackageName());
    }
    @Test
    public void setLocation(){

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(9.931233);
        location.setLongitude(76.267303);
        mActivityRule.getActivity().mUserLocation = null;
        SystemClock.sleep(4000);

        mActivityRule.getActivity().mLocationListener.onSuccess(location);
        SystemClock.sleep(50000);

    }


}
