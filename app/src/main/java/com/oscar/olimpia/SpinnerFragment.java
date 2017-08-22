package com.oscar.olimpia;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpinnerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpinnerFragment extends DialogFragment implements AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.listView)
    ListView listView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private String[] listitemsPais = {"Colombia", "Brasil"};
    private String[] listitemsColombiaCiudad = {"Bogota", "Medellin", "Cali"};
    private String[] listitemsBrasilCiudad = {"Rio de janeiro", "Sao Paulo", "Salvador"};

    public SpinnerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpinnerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpinnerFragment newInstance(String param1, String param2) {
        SpinnerFragment fragment = new SpinnerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spinner, container, false);
        ButterKnife.bind(this, view);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String cadena) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(cadena);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        switch (mParam1){
            case "pais":
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, listitemsPais);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);
                break;
            case "Colombia":
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, listitemsColombiaCiudad);
                listView.setAdapter(adapter1);
                listView.setOnItemClickListener(this);
                break;
            case "Brasil":
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, listitemsBrasilCiudad);
                listView.setAdapter(adapter2);
                listView.setOnItemClickListener(this);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        dismiss();

        switch (mParam1) {
            case "pais":
                mListener.onFragmentInteraction(listitemsPais[i], true);
                break;
            case "Colombia":
                mListener.onFragmentInteraction(listitemsColombiaCiudad[i], false);
                break;
            case "Brasil":
                mListener.onFragmentInteraction(listitemsBrasilCiudad[i], false);
                break;
        }
        //mListener.onFragmentInteraction(listitemsColombiaCiudad[i], true);
        //Toast.makeText(getActivity(), listitemsColombiaCiudad[i], Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String cadena, boolean isPais);
    }
}
