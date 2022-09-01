package kr.ac.daejin.sauceApp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class tab_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        ViewPager vp = findViewById(R.id.viewpager);
        fragmentAdapter adapter = new fragmentAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(vp);

//        ArrayList<Integer> images = new ArrayList<>();
//        images.add(R.drawable.cal);
//        images.add(R.drawable.sea);
//        images.add(R.drawable.set);

//        for(int i=0; i<3; i++) tab.getTabAt(i).setIcon(images.get(i));

    }
}