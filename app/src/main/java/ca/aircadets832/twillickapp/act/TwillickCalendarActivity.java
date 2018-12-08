package ca.aircadets832.twillickapp.act;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import ca.aircadets832.twillickapp.R;

public class TwillickCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_twillick_calendar);
        String url = "https://calendar.google.com/calendar/embed?showTitle=0&showNav=0&showDate=0&showPrint=0&showTabs=0&showCalendars=0&showTz=0&mode=AGENDA&src=832aircadets.ca_btk4j8j1fvgaq515hqs15oeg58%40group.calendar.google.com&ctz=America%2FToronto";

        WebView wvCal= findViewById(R.id.wvCalendar);
        wvCal.setWebViewClient(new WebViewClient());
        wvCal.setWebChromeClient(new WebChromeClient());
        wvCal.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        wvCal.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings webSettings = wvCal.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvCal.loadUrl(url);
    }

}
