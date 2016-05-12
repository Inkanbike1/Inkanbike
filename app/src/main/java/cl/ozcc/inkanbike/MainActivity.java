package cl.ozcc.inkanbike;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cl.ozcc.inkanbike.objects.DataHelper;
import cl.ozcc.inkanbike.objects.MessageUI;
import cl.ozcc.inkanbike.objects.User;
import cl.ozcc.inkanbike.objects.Valid;

public class MainActivity extends AppCompatActivity {

    private final static Valid validObj = new Valid();
    static User user = new User();
    private  static MessageUI msjUi;
    private static Intent inkanAct;

    //poiuytrew


    /**
     *  The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msjUi = new MessageUI(MainActivity.this);
        inkanAct = new Intent(MainActivity.this, InkanActivity.class);

        // Intent registerToken = new Intent(getApplicationContext(), RegistrationIntentService.class);
        // startService(registerToken);

        if(!new DataHelper(getApplicationContext()).ValidLogin()){
            if(!validObj.ValidNet()){
                msjUi.InflateToast("NO NET");
                finish();
            }
        }else{
            startActivity(inkanAct);
            finish();
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }
    @Override
    public void onBackPressed() {
        switch (mViewPager.getCurrentItem()){
            case 0:
                super.onBackPressed();
                break;
            case 1:
                mViewPager.arrowScroll(View.FOCUS_LEFT);
                break;
        }
    }
    public void ChangeRight(View v){
        mViewPager.arrowScroll(View.FOCUS_RIGHT);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView;
            final Context ctx;
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.fragment_main_one, container, false);
                ctx = rootView.getContext();

                final EditText email = (EditText) rootView.findViewById(R.id.emailIng);
                final EditText pass = (EditText) rootView.findViewById(R.id.passIng);

                Button login = (Button) rootView.findViewById(R.id.btnIngresar);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int usrId = new Valid().ValidLogin(email.getText().toString(), pass.getText().toString(), getContext());

                        if (usrId != 0) {
                            Intent login = new Intent(ctx, InkanActivity.class);
                            ctx.startActivity(login);

                            new DataHelper(ctx).setLogin();
                            user.findUserFromServer(usrId).save(ctx);
                            getActivity().finish();

                        } else {
                            msjUi.InflateDialog(ctx.getString(R.string.login_title), ctx.getString(R.string.LOGIN_FAIL));
                        }
                    }
                });
            } else {
                rootView = inflater.inflate(R.layout.fragment_main_two, container, false);
                ctx = rootView.getContext();
                final EditText nameReg = (EditText) rootView.findViewById(R.id.newName);
                final EditText emailReg = (EditText) rootView.findViewById(R.id.newEmail);
                final EditText passReg = (EditText) rootView.findViewById(R.id.newPass);
                final EditText pass2Reg = (EditText) rootView.findViewById(R.id.newPass2);

                emailReg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            if (!validObj.ValidEmail(emailReg.getText().toString())) {
                                Toast.makeText(getContext(), getString(R.string.EMAIL_REGISTERED), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                Button register = (Button) rootView.findViewById(R.id.btnRegistroIng);
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User.email = emailReg.getText().toString();
                        if (passReg.getText().toString().equals(pass2Reg.getText().toString())) {
                            if (validObj.ValidEmail(User.email)) {

                                User.name = nameReg.getText().toString();
                                User.pass = passReg.getText().toString();
                                User.email = emailReg.getText().toString();
                                User.token = "EXAMPLE_STRING";
                                if (user.Register()) {
                                    startActivity(inkanAct);
                                    new DataHelper(getContext()).setLogin();
                                    User.id = new Valid().ValidLogin(User.email, User.pass, ctx);
                                    user.save(getContext());
                                    getActivity().finish();
                                }
                            } else {
                                Toast.makeText(getContext(), getString(R.string.EMAIL_REGISTERED), Toast.LENGTH_LONG).show();
                                User.email = "";
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.PASS_NOT_EQUALS), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show total pages.
            return 2;
        }
    }
}