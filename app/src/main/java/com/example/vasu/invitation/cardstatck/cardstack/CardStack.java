package com.example.vasu.invitation.cardstatck.cardstack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class CardStack extends RelativeLayout {
    ArrayList<View> viewCollection = new ArrayList<View>();
    private int mIndex = 0;
    private int mNumVisible = 4;
    private Adapter mAdapter;
    private OnTouchListener mOnTouchListener;
    //private Queue<View> mIdleStack = new Queue<View>;
    private CardAnimator mCardAnimator;
    private CardEventListener mEventListener = new DefaultStackEventListener(10);
    private int mContentResource = 0;

    //method used to descard the top view based on direction...which is going to use by other widget
    //eg: using button we can call this method and we can pass the direction as parameter eg: 0 or 2 to discard the card...
    private DataSetObserver mOb = new DataSetObserver() {
        @Override
        public void onChanged() {
            reset(false);
        }
    };


    //return the current index of card

    //only necessary when I need the attrs from xml, this will be used when inflating layout
    public CardStack(Context context, AttributeSet attrs) {
        super(context, attrs);


        //get attrs assign minVisiableNum
        for (int i = 0; i < mNumVisible; i++) {
            addContainerViews();
        }
        setupAnimation();
    }

    public CardStack(Context context) {
        super(context);
    }

    public void discardTop(final int direction) {
        mCardAnimator.discard(direction, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator arg0) {
                mCardAnimator.initLayout();
                mIndex++;
                mEventListener.discarded(mIndex, direction);

                //mIndex = mIndex%mAdapter.getCount();
                loadLast();

                viewCollection.get(0).setOnTouchListener(null);
                viewCollection.get(viewCollection.size() - 1)
                        .setOnTouchListener(mOnTouchListener);
            }
        });
    }

    public int getCurrIndex() {
        //sync?
        return mIndex;
    }

    private void addContainerViews() {
        FrameLayout v = new FrameLayout(getContext());
        viewCollection.add(v);
        addView(v);
    }

    public void setStackMargin(int margin) {
        mCardAnimator.setStackMargin(margin);
        mCardAnimator.initLayout();
    }

    public void setContentResource(int res) {
        mContentResource = res;
    }

    //when need to reset the card into stack
    public void reset(boolean resetIndex) {
        if (resetIndex) mIndex = 0;
        removeAllViews();
        viewCollection.clear();
        for (int i = 0; i < mNumVisible; i++) {
            addContainerViews();
        }
        setupAnimation();
        loadData();
    }

    public void setVisibleCardNum(int visiableNum) {
        mNumVisible = visiableNum;
        reset(false);
    }


    //setting the card animator and putting GestureDetectorListener and overriding the methods of DragListener interface
    //using this method can get to know the dragged card location
    //see the animation effect on that selected card

    public void setThreshold(int t) {
        mEventListener = new DefaultStackEventListener(t);
    }

    public void setListener(CardEventListener cel) {
        mEventListener = cel;
    }


    //ArrayList

    private void setupAnimation() {
        final View cardView = viewCollection.get(viewCollection.size() - 1);
        mCardAnimator = new CardAnimator(viewCollection); //creating an object of cardAnimator
        mCardAnimator.initLayout(); //initialize the cardAnimator using object

        final DragGestureDetector dd = new DragGestureDetector(CardStack.this.getContext(), new DragGestureDetector.DragListener() {


            //when drag of card will start this method invoke first
            @Override
            public boolean onDragStart(MotionEvent e1, MotionEvent e2,
                                       float distanceX, float distanceY) {
                mCardAnimator.drag(e1, e2, distanceX, distanceY);
                return true;
            }

            //user dragging the card continue then this method will invoke
            @Override
            public boolean onDragContinue(MotionEvent e1, MotionEvent e2,
                                          float distanceX, float distanceY) {
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                //float distance = CardUtils.distance(x1,y1,x2,y2);
                final int direction = CardUtils.direction(x1, y1, x2, y2);
                mCardAnimator.drag(e1, e2, distanceX, distanceY);
                mEventListener.swipeContinue(direction, Math.abs(x2 - x1), Math.abs(y2 - y1));
                return true;
            }

            /*when user dum the card then this method will invoke
            * here it will check the distance between the direction and the distance of the card
            *and based on that this method discard or reverse the card
            */
            @Override
            public boolean onDragEnd(MotionEvent e1, MotionEvent e2) {
                //reverse(e1,e2);
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                Log.d("onDragEndMethod", "dis:s");
                float distance = CardUtils.distance(x1, y1, x2, y2);
                final int direction = CardUtils.direction(x1, y1, x2, y2);
                Log.d("onDragEndMethod", "dis: " + distance + "  direction: " + direction);
                boolean discard = mEventListener.swipeEnd(direction, distance);
                if (discard) {
                    Log.d("onDragEndMethod", "deleted");
                    mCardAnimator.discard(direction, new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator arg0) {
                            mCardAnimator.initLayout();
                            mIndex++;
                            mEventListener.discarded(mIndex, direction);

                            //mIndex = mIndex%mAdapter.getCount();
                            loadLast();

                            viewCollection.get(0).setOnTouchListener(null);
                            viewCollection.get(viewCollection.size() - 1)
                                    .setOnTouchListener(mOnTouchListener);
                        }

                    });
                } else {
                    Log.d("onDragEndMethod", "reverse");
                    mCardAnimator.reverse(e1, e2);
                }
                return true;
            }

            @Override
            public boolean onTapUp() {
                mEventListener.topCardTapped();
                return true;
            }
        }
        );

        mOnTouchListener = new OnTouchListener() {
            private static final String DEBUG_TAG = "MotionEvents";

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                dd.onTouchEvent(event);
                return true;
            }
        };
        cardView.setOnTouchListener(mOnTouchListener);
    }

    //used to set Adapter
    public void setAdapter(final Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mOb);
        }
        mAdapter = adapter;
        adapter.registerDataSetObserver(mOb);

        loadData();
    }

    //call to load data based on index
    //this method will put Gone or visible property to view...
    private void loadData() {
        for (int i = mNumVisible - 1; i >= 0; i--) {
            ViewGroup parent = (ViewGroup) viewCollection.get(i);
            int index = (mIndex + mNumVisible - 1) - i;
            if (index > mAdapter.getCount() - 1) {
                parent.setVisibility(View.GONE);
            } else {
                View child = mAdapter.getView(index, getContentView(), this);
                parent.addView(child);
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

    //returning a new view even
    private View getContentView() {
        View contentView = null;
        if (mContentResource != 0) {
            LayoutInflater lf = LayoutInflater.from(getContext());
            contentView = lf.inflate(mContentResource, null);
        }
        return contentView;

    }

    private void loadLast() {
        ViewGroup parent = (ViewGroup) viewCollection.get(0);

        int lastIndex = (mNumVisible - 1) + mIndex;
        if (lastIndex > mAdapter.getCount() - 1) {
            parent.setVisibility(View.GONE); //hiding the top view and returning
            return;
        }

        View child = mAdapter.getView(lastIndex, getContentView(), parent);
        parent.removeAllViews();// remove all previous view
        parent.addView(child); // and then adding new
    }

    //return the size of stack
    public int getStackSize() {
        return mNumVisible;
    }

    public interface CardEventListener {
        //section
        // 0 | 1
        //--------
        // 2 | 3
        // swipe distance, most likely be used with height and width of a view ;

        boolean swipeEnd(int section, float distance);

        boolean swipeStart(int section, float distance);

        boolean swipeContinue(int section, float distanceX, float distanceY);

        void discarded(int mIndex, int direction);

        void topCardTapped();
    }
}
