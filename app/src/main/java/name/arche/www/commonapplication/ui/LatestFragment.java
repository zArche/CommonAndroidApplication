package name.arche.www.commonapplication.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import name.arche.www.commonapplication.R;

/**
 * Created by Arche on 2015/11/4.
 */
public class LatestFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content,null);
        ((TextView)view.findViewById(R.id.tv_content)).setText("最新");

        return view;
    }
}
