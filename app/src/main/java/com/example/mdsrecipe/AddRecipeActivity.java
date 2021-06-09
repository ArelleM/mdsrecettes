package com.example.mdsrecipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mdsrecipe.Helper.DBHelper;
import com.example.mdsrecipe.Helper.ImageHelper;
import com.example.mdsrecipe.Model.RecipeItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class AddRecipeActivity extends AppCompatActivity {


    private static SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private Button btn_addNewRecipe;
    private EditText newName;
    private TextView newAuthor;
    private Spinner newCounty;
    private ImageView newMainImg;
    private EditText newDescription;
    private ListView newIngredientList;
    private EditText newHowto;

    private ImageView navi1;
    private ImageView navi2;
    private ImageView navi3;

    ImageHelper imageHelper = new ImageHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        navi1 = findViewById(R.id.imgv_Add_navi1);
        navi2 = findViewById(R.id.imgv_Add_navi2);
        navi3 = findViewById(R.id.imgv_Add_navi3);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.add(new AddRecipeFragment());
        mSectionsPagerAdapter.add(new AddIngredientFragment());
        mSectionsPagerAdapter.add(new AddHowtoFragment());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                {
                    navi1.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.greenbutton));
                    navi2.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.graybackground));
                    navi3.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.graybackground));
                }
                else if (position == 1)
                {
                    navi1.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.graybackground));
                    navi2.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.greenbutton));
                    navi3.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.graybackground));
                }
                else
                {
                    navi1.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.graybackground));
                    navi2.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.graybackground));
                    navi3.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.greenbutton));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_addNewRecipe = findViewById(R.id.btn_Add_recipeAdd);
        btn_addNewRecipe.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                newMainImg = findViewById(R.id.imgv_Add_Image);
                newName = findViewById(R.id.txt_Add_NewName);
                newAuthor = findViewById(R.id.txt_Add_Author);
                newCounty = findViewById(R.id.spinner_Add_Country);
                newDescription = findViewById(R.id.txt_Add_Description);
                newIngredientList = findViewById(R.id.ListView_Add_Ingredient);
                newHowto = findViewById(R.id.txt_ADD_Howto);

                byte[] makeMainImg;
                byte[] makeThumbnail;
                String makeRecipeName;
                String makeDescription;
                String makeAuthor;
                String makeCategory;
                String makeHowto;
                Date today = new Date();
                ArrayList<String> makeIndeList = new ArrayList<String>();

                if(newMainImg.getDrawable() != null)
                {
                    BitmapDrawable d = (BitmapDrawable)((ImageView) findViewById(R.id.imgv_Add_Image)).getDrawable();
                    Bitmap bitmap = d.getBitmap();
                    Bitmap thBitmap = imageHelper.getThubmail(bitmap);
                    makeMainImg = imageHelper.getByteArrayFromBitmap(bitmap);
                    makeThumbnail = imageHelper.getByteArrayFromBitmap(thBitmap);
                }
                else
                {
                    Toast.makeText(v.getContext(), "Veuillez ajouter une image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newName.getText() != null && !newName.getText().toString().isEmpty()){
                    makeRecipeName = newName.getText().toString();
                    //Toast.makeText(v.getContext(), makeRecipeName, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(v.getContext(), "Veuillez donner un nom à votre recette", Toast.LENGTH_SHORT).show();
                    return;
                }

                makeAuthor = newAuthor.getText().toString();

                if (newCounty.getSelectedItem() != null && !newCounty.getSelectedItem().toString().isEmpty()){
                    makeCategory = newCounty.getSelectedItem().toString();
                    //Toast.makeText(v.getContext(), makeCategory, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(v.getContext(), "Veuillez sélectionner une catégorie", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newDescription.getText() != null && !newDescription.getText().toString().isEmpty()){
                    makeDescription = newDescription.getText().toString();
                    //Toast.makeText(v.getContext(), makeRecipeName, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(v.getContext(), "Veuillez ajouter une description", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (newIngredientList.getCount() > 0 )
                {
                    for (int i=0; i <newIngredientList.getCount(); i++)
                    {
                        makeIndeList.add(newIngredientList.getItemAtPosition(i).toString());
                    }

                    //Toast.makeText(v.getContext(), makeIndeList.get(0).toString(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(v.getContext(), "Veuillez ajouter 1 ou plusieurs ingrédients", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newHowto.getText() != null && !newHowto.getText().toString().isEmpty()){
                    makeHowto = newHowto.getText().toString();
                    //Toast.makeText(v.getContext(), makeRecipeName, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(v.getContext(), "Veuillez ajouter les instructions pour effecter votre recette", Toast.LENGTH_SHORT).show();
                    return;
                }


                //Toast.makeText(v.getContext(), newName.getText(), Toast.LENGTH_SHORT).show();
                //Connect DB
                DBHelper dbHelper = new DBHelper(v.getContext(), "Recipes.db", null, 1);

                dbHelper.recipes_Insert(makeCategory, makeRecipeName, makeAuthor, today.toString(),
                makeHowto, makeDescription,
                makeThumbnail, makeMainImg, 0);
                int makeRecipeid = dbHelper.recipes_GetIdByName(makeRecipeName);
                if (makeRecipeid != -1)
                {
                   for(int i=0; i < makeIndeList.size(); i++)
                   {
                       dbHelper.ingredients_Insert(makeRecipeid, makeIndeList.get(i));
                   }

                    Toast.makeText(v.getContext(), "Recette ajoutée", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(v.getContext(), "Erreur", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        public PlaceholderFragment() {

        }

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
            View rootView = inflater.inflate(R.layout.fragment_add_recipe, container, false);

            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }





    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> _fragments;

        public SectionsPagerAdapter(FragmentManager fm)
        {
           super(fm);
            this._fragments = new ArrayList<Fragment>();
        }

        public void add(Fragment fragment) {
            this._fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return  this._fragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }
}
