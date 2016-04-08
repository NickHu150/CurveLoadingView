package zrzn.com.damping_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zrzn.com.damping_view.fruitview.CurveHeadLoadingView;
import zrzn.com.damping_view.fruitview.FruitView;

/**
 * Created by 胡亚敏 on 2016/3/25.
 */
public class TabFragment extends Fragment {

   static TextView textView;
    CurveHeadLoadingView curveView;
    private static TabFragment tabFragment;
    static String string1;
    String[] strarr = {"1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.test,null);
        FruitView fruitView = (FruitView) view.findViewById(R.id.fv);


        return view;
    }

    public static Fragment newInstance(String string){
        tabFragment = new TabFragment();
        string1 = string;
        return tabFragment;
    }


}
