package com.example.insta.utils;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.insta.GonderiActivity;
import com.example.insta.R;
import com.example.insta.interfacee.EditImageFragmentListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditImageFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
private EditImageFragmentListener listener;
SeekBar seekBar_brightness, seekbar_constrant, seekbar_saturation;

    public void setListener(GonderiActivity listener) {
        this.listener = listener;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditImageFragment newInstance(String param1, String param2) {
        EditImageFragment fragment = new EditImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View itemView= inflater.inflate(R.layout.fragment_edit_image, container, false);
       seekBar_brightness=(SeekBar)itemView.findViewById(R.id.seekbar_brightness);
        seekbar_saturation=(SeekBar)itemView.findViewById(R.id.seekbar_saturation);
        seekbar_constrant=(SeekBar)itemView.findViewById(R.id.seekbar_constraint);

        seekBar_brightness.setMax(200);
        seekBar_brightness.setProgress(100);

        seekbar_constrant.setMax(20);
        seekbar_constrant.setProgress(0);

        seekbar_saturation.setMax(30);
        seekbar_saturation.setProgress(10);

        seekbar_saturation.setOnSeekBarChangeListener(this);
        seekbar_constrant.setOnSeekBarChangeListener(this);
        seekBar_brightness.setOnSeekBarChangeListener(this);
return itemView;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    if(listener!=null){
    if(seekBar.getId()==R.id.seekbar_brightness){


        listener.onBrightnessChanged(progress-100);
    }
    else if(seekbar_constrant.getId()==R.id.seekbar_constraint){
        progress+=10;
       float value= .10f*progress;
       listener.onConstrantChanged(value);
    }

    }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    if(listener!=null)

    listener.onEditStarted();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    if (listener!=null)
        listener.onEditCompleted();
    }
    public void  resetControls(){
        seekBar_brightness.setProgress(100);
        seekbar_constrant.setProgress(0);
        seekbar_saturation.setProgress(10);
    }
}