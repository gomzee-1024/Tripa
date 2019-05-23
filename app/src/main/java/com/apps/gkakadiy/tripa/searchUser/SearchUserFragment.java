package com.apps.gkakadiy.tripa.searchUser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apps.gkakadiy.tripa.Constants;
import com.apps.gkakadiy.tripa.Injection;
import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.data.UserPublic;
import com.apps.gkakadiy.tripa.userprofile.UserProfileActivity;
import com.apps.gkakadiy.tripa.utils.ClickListener;
import com.apps.gkakadiy.tripa.utils.RecyclerTouchListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchUserFragment extends Fragment implements SearchUserContract.SearchUserBaseView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText mSearchText;
    private ImageView mClearTextButton;
    private RecyclerView mUserRecyclerView;
    private SearchUserRecyclerViewAdapter mSearchUserAdapter;
    private ArrayList<UserPublic> mSearchUserList;
    private SearchUserPresenter mSearchUserPresenter;
    private OnFragmentInteractionListener mListener;
    private View.OnClickListener mClearButtonListener;
    private TextWatcher mTextWatcher;
    private TextView.OnEditorActionListener mSearchButtonListener;
    private ProgressBar mProgressBar;
    private RecyclerTouchListener mRecyclerTouchListener;
    public SearchUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchUserFragment newInstance(String param1, String param2) {
        SearchUserFragment fragment = new SearchUserFragment();
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
        return inflater.inflate(R.layout.fragment_search_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClearTextButton =  view.findViewById(R.id.search_user_clear_button);
        mSearchText = view.findViewById(R.id.search_user_search_text);
        mUserRecyclerView = view.findViewById(R.id.search_user_recycler_view);
        mProgressBar = view.findViewById(R.id.search_user_progress_bar);
        mSearchUserPresenter = new SearchUserPresenter(Injection.provideUserRepository(),this);
        mSearchUserList = new ArrayList<>();
        mSearchUserAdapter = new SearchUserRecyclerViewAdapter(getActivity(),mSearchUserList);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(),RecyclerView.VERTICAL,false));
        mUserRecyclerView.setAdapter(mSearchUserAdapter);
        mUserRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), mUserRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserPublic user = mSearchUserList.get(position);
                showUserProfile(user.getUser_id());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0){
                    showClearButton();
                    //mSearchUserAdapter.clearUserList();
                    //mSearchUserPresenter.searchUsers(s.toString().toLowerCase());
                }else{
                    //mSearchUserAdapter.clearUserList();
                    hideClearButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("SearchUserFragment","AfterTextChanged "+s);
            }
        };
        //mSearchText.addTextChangedListener(mTextWatcher);
        mClearButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.setText("");
            }
        };
        mClearTextButton.setOnClickListener(mClearButtonListener);
        mSearchButtonListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (mProgressBar.getVisibility() == View.INVISIBLE) {
                        if (!v.getText().toString().equalsIgnoreCase("")) {
                            mSearchUserAdapter.clearUserList();
                            mSearchUserPresenter.searchUsers(v.getText().toString().toLowerCase());
                        }
                    }
                    return true;
                }
                return false;
            }
        };
        mSearchText.setOnEditorActionListener(mSearchButtonListener);
    }

    private void showUserProfile(String userId){
        Intent intent = new Intent(getContext().getApplicationContext(),UserProfileActivity.class);
        intent.putExtra(Constants.DATA_USER_ID,userId);
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        mSearchUserAdapter.cleanup();
        mSearchUserPresenter.unsubscribe();
        mSearchText.removeTextChangedListener(mTextWatcher);
        mSearchButtonListener = null;
        mTextWatcher=null;
        mListener = null;
    }

    @Override
    public void showClearButton() {
        mClearTextButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideClearButton() {
        mClearTextButton.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void addUserToView(UserPublic user) {
        mSearchUserAdapter.addUser(user);
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
        void onFragmentInteraction(Uri uri);
    }
}
