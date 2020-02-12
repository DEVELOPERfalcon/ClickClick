package kr.co.taksoft.clickclick;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    LinearLayout bg;

    TextView tv;
    TextView score;
    ImageView start;
    TextView second;
    ImageView[] images;
    AdView adView;

    boolean checkStart = false;
    int num = 1;
    int currentScore = 0;
    int time = 30;
    Thread timeThread;

    Random rand;
    int[] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bg = findViewById(R.id.bg);

        tv = findViewById(R.id.tv);
        score = findViewById(R.id.score);
        start = findViewById(R.id.image00);
        second = findViewById(R.id.second);
        start.setOnClickListener(gameStart);
        timeThread = new TimeThread();

        images = new ImageView[12];
        for(int i=0;i<images.length;i++){
            images[i] = findViewById(R.id.image01+i);
            images[i].setOnClickListener(listener);
        }

        rand = new Random();
        arr = new int[12];
        for(int i=0;i<arr.length;i++){
            arr[i] = rand.nextInt(12);
            for(int k=0;k<i;k++){
                if(arr[i] == arr[k]){
                    i--;
                    break;
                }
            }
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView = findViewById(R.id.adview);
        adView.setAdUnitId("ca-app-pub-1785598529343763/7208501486");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    View.OnClickListener gameStart = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tv.setText("1부터 순서대로 클릭하세요");
            start.setImageResource(R.drawable.ing);
            for(int i=0;i<images.length;i++){
                images[i].setImageResource(R.drawable.num01+arr[i]);
                images[i].setTag(arr[i]+1);
            }
            start.setEnabled(false);

            timeThread.start();
        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            ImageView image = findViewById(id);
            if(Integer.parseInt(image.getTag().toString()) == num){
                image.setVisibility(View.INVISIBLE);
                num++;
                currentScore++;
                score.setText("SCORE: "+currentScore);
            }else{
                if(currentScore<=0){
                    currentScore=0;
                    score.setText("SCORE: "+currentScore);
                }else{
                    currentScore--;
                    score.setText("SCORE: "+currentScore);
                }
            }
            if(num>12){
                num=1;
                for(int i=0;i<arr.length;i++){
                    arr[i] = rand.nextInt(12);
                    for(int k=0;k<i;k++){
                        if(arr[i] == arr[k]){
                            i--;
                            break;
                        }
                    }
                }
                for(int i=0;i<images.length;i++){
                    images[i].setImageResource(R.drawable.num01+arr[i]);
                    images[i].setTag(arr[i]+1);
                    images[i].setVisibility(View.VISIBLE);
                }

            }
        }
    };

    class TimeThread extends Thread {
        @Override
        public void run() {
            while (time != 0){
                try {
                    Thread.sleep(1000);
                    time--;
                    second.setText(time+"초");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tv.setVisibility(View.INVISIBLE);
            start.setVisibility(View.INVISIBLE);
            for(int i=0;i<images.length;i++){
                images[i].setVisibility(View.INVISIBLE);
            }
            second.setVisibility(View.INVISIBLE);
            bg.setBackgroundResource(R.drawable.bg4);
        }
    }

}
