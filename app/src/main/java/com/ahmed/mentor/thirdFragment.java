package com.ahmed.mentor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link thirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class thirdFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth fireBase = FirebaseAuth.getInstance();
    private String userID = fireBase.getCurrentUser().getUid();

    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = fireStore.collection("Users").document(userID).collection("Interests");

    private TextView interest,sports,music,videogames, tvshows,boardgames, cooking,reading,art;
    private SeekBar sportsB, musicB, videogamesB, tvshowsB, boardgamesB, cookingB, readingB, artB;
    private Button submit;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public thirdFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static thirdFragment newInstance(String param1, String param2) {
        thirdFragment fragment = new thirdFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = (ViewGroup)inflater.inflate(R.layout.fragment_third, container, false);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots.size() != 1)
                {
                    interest = root.findViewById(R.id.InterestTV);  interest.setText("Choose your interests");
                    sports = root.findViewById(R.id.sportsTV);  sports.setText("Sports");
                    music = root.findViewById(R.id.musicTV);    music.setText("Music");
                    videogames = root.findViewById(R.id.videoGamesTV);  videogames.setText("Video Games");
                    tvshows = root.findViewById(R.id.tvshowsTV);    tvshows.setText("TV Shows");
                    boardgames = root.findViewById(R.id.boardGamesTV);  boardgames.setText("Board Games");
                    cooking = root.findViewById(R.id.cookingTV);    cooking.setText("Cooking");
                    reading = root.findViewById(R.id.readingTV);    reading.setText("Reading");
                    art = root.findViewById(R.id.artTV);    art.setText("Art");
                    submit = root.findViewById(R.id.submitBTN); submit.setText("Submit");

                    sportsB = root.findViewById(R.id.sportsSB);
                    musicB = root.findViewById(R.id.musicSB);
                    videogamesB = root.findViewById(R.id.videoGamesSB);
                    tvshowsB = root.findViewById(R.id.tvShowsSB);
                    boardgamesB = root.findViewById(R.id.boardGamesSB);
                    cookingB = root.findViewById(R.id.cookingSB);
                    readingB = root.findViewById(R.id.readingSB);
                    artB = root.findViewById(R.id.artSB);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CollectionReference cr = collectionReference;
                            Map<String,Object> user = new HashMap<>();
                            user.put(sports.getText().toString(), new Double(sportsB.getProgress()));
                            user.put(music.getText().toString(), new Double(musicB.getProgress()));
                            user.put(videogames.getText().toString(), new Double(videogamesB.getProgress()));
                            user.put(tvshows.getText().toString(), new Double (tvshowsB.getProgress()));
                            user.put(boardgames.getText().toString(), new Double (boardgamesB.getProgress()));
                            user.put(cooking.getText().toString(), new Double(cookingB.getProgress()));
                            user.put(reading.getText().toString(), new Double(readingB.getProgress()));
                            user.put(art.getText().toString(), new Double(artB.getProgress()));
                            //user.put("Submitted", true);
                            cr.add(user);

                            Toast.makeText(getActivity().getApplicationContext(),"Submitted!", Toast.LENGTH_SHORT).show();
                            //DocumentReference documentReference = fireStore.collection("Users").document(userID);
                        }
                    });
                }
                else
                {
                    Log.d("SIZE OF QUERY", String.valueOf(queryDocumentSnapshots.size()));
                    interest = root.findViewById(R.id.InterestTV);  interest.setText("You have already chose your interests!");
                    sportsB = root.findViewById(R.id.sportsSB); sportsB.setAlpha(0);
                    musicB = root.findViewById(R.id.musicSB); musicB.setAlpha(0);
                    videogamesB = root.findViewById(R.id.videoGamesSB); videogamesB.setAlpha(0);
                    tvshowsB = root.findViewById(R.id.tvShowsSB); tvshowsB.setAlpha(0);
                    boardgamesB = root.findViewById(R.id.boardGamesSB); boardgamesB.setAlpha(0);
                    cookingB = root.findViewById(R.id.cookingSB); cookingB.setAlpha(0);
                    readingB = root.findViewById(R.id.readingSB); readingB.setAlpha(0);
                    artB = root.findViewById(R.id.artSB); artB.setAlpha(0);

                    submit = root.findViewById(R.id.submitBTN); submit.setAlpha(0);
                }
            }
        });



        return root;
    }
}