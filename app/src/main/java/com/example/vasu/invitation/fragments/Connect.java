package com.example.vasu.invitation.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vasu.invitation.R;
import com.example.vasu.invitation.adapters.CardStackAdapter;
import com.example.vasu.invitation.animation.RippleAnimation;
import com.example.vasu.invitation.cardstatck.cardstack.CardStack;
import com.example.vasu.invitation.cardstatck.cardstack.DefaultStackEventListener;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by Aradh Pillai on 2/10/15.
 */

/*
* this fragment class is using to show the list of connection into stackCard  based on users location
* and user will have option to discard and like the connection.
*
*
* */
public class Connect extends Fragment {

    private static final String TAG = "Connect";
    RippleAnimation rippleBackground1, rippleBackground2;

    CardStack cardStack;
    //this class is using for swipe the AdapterView
    CardStackAdapter mCardAdapter;
    private Bitmap bmp;
    private ImageView img;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        init(view);// to initialize widgets
        doRippleBackground(); //start ripple background work..
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //img = (ImageView) getActivity().findViewById(R.id.connect_imgsample);

       /* mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };


        mAuth.addAuthStateListener(authStateListener);

        mAuth.signInAnonymously()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://invitation-d9f7e.appspot.com");

                        StorageReference imagesRef = storageRef.child("epmc.jpg");
                        final long ONE_MEGABYTE = 1024 * 1024;
                        imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Data for "images/island.jpg" is returns, use this as needed
                                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                img.setImageBitmap(bmp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: ", e);
                            }
                        });

                    }
                });*/

     /*   FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://invitation-d9f7e.appspot.com");

        StorageReference imagesRef = storageRef.child("epmc.jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });*/


    }


    @Override
    public void onStop() {
        super.onStop();
      /*  if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }*/

    }

    private void init(View view) {
        //root ripple background initialization
        rippleBackground1 = (RippleAnimation) view.findViewById(R.id.content);

        //child ripple background initialization
        // rippleBackground2 = (RippleBackground) view.findViewById(R.id.content2);

        //cardStack initialization
        cardStack = (CardStack) view.findViewById(R.id.frame);

        //at begin setting rippleBackground visibility as VISIBLE and setting CardStack visibility as GONE
        rippleBackground1.setVisibility(View.VISIBLE);
        cardStack.setVisibility(View.GONE);

        //creating adapter
        mCardAdapter = new CardStackAdapter(getActivity().getApplicationContext());
    }

    public void doRippleBackground() {


        //start ripple background animations
        startAnimation();

        //handler created to handle cardStack as well as timer...
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                callCardStack();
            }
        }, 2000);

    }

    //start the background ripple animation...
    private void startAnimation() {
        //if it's not running
        if (!rippleBackground1.isRippleAnimationRunning()) {
            rippleBackground1.startRippleAnimation();//start root ripple animation
            // rippleBackground2.startRippleAnimation();//start child ripple animation
        }
    }

    //this method will stop background ripple animation. if it's running.
    private void stopAnimation() {
        if (rippleBackground1.isRippleAnimationRunning()) {
            rippleBackground1.stopRippleAnimation();
            // rippleBackground2.stopRippleAnimation();
        }
    }

    //cardStack view will set it as visible and load the information into stack.
    public void callCardStack() {

        cardStack.setVisibility(View.VISIBLE);
        rippleBackground1.setVisibility(View.GONE);

        stopAnimation();//start the ripple background animation.

        //Setting Resource of CardStack
        cardStack.setContentResource(R.layout.card_stack_item);

        //Adding 30 dummy info for CardStack
        cardStack.setAdapter(mCardAdapter);

        //Setting Listener and passing distance as a parameter ,
        //based on the distance card will discard
        //if dragging card distance would be more than specified distance(100) then card will discard or else card will reverse on same position.
        cardStack.setListener(new DefaultStackEventListener(300));

    }
}
