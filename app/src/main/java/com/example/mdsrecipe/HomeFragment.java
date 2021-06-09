package com.example.mdsrecipe;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mdsrecipe.Helper.DBHelper;
import com.example.mdsrecipe.Helper.ImageHelper;
import com.example.mdsrecipe.Model.CategoryItem;
import com.example.mdsrecipe.Model.RecipeItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static com.example.mdsrecipe.R.drawable.*;



public class HomeFragment extends Fragment {
    ImageHelper imageHelper = new ImageHelper();
    ArrayList<RecipeItem> bestList;
    ArrayList<RecipeItem> newList;

    Date today = new Date();

    public HomeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);


        FloatingActionButton fab =  view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check search recipes menu on the slide menu
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_search);
                //connect to search recipes page
                FragmentManager manager = getActivity().getSupportFragmentManager();
                SearchFragment searchFragment = new SearchFragment();
                manager.beginTransaction().replace(R.id.root_layout, searchFragment, searchFragment.getTag()).addToBackStack(null).commit();
            }
        });
        GridView newGridView = (GridView)view.findViewById(R.id.GridView_New);

        //Connect DB
        DBHelper dbHelper = new DBHelper(getContext(), "Recipes.db", null, 1);


        ArrayList<RecipeItem> defaultDataList = dbHelper.recipes_SelectAll();
        if(defaultDataList == null || defaultDataList.size() == 0)
        {


            //Set Bolognese data
             Drawable drawable = getResources().getDrawable(R.drawable.bolognese, getActivity().getTheme());
            byte[] bolognese = imageHelper.getByteArrayFromDrawable(drawable);

            dbHelper.recipes_Insert("Italien", "Spaghettis bolognaise", "Mamie", today.toString(),
                    "ÉTAPE 1\n" +
                            "Hachez la sauge, le basilic et le persil.\n" +
                            "\n" +
                            "ÉTAPE 2\n" +
                            "Hachez les oignons, découpez le céleri en cubes.\n" +
                            "\n" +
                            "ÉTAPE 3\n" +
                            "Détaillez les champignons en cubes également.\n" +
                            "\n" +
                            "ÉTAPE 4\n" +
                            "Faites chauffer 3 cuillerées à soupe d'huile d'olive dans une cocotte, mettez-y les oignons, le céleri et les champignons.\n" +
                            "\n" +
                            "ÉTAPE 5\n" +
                            "Ajoutez l'ail écrasé, le concentré de tomate, la sauce tomate, la viande hachée et pressez bien le tout.\n" +
                            "\n" +
                            "ÉTAPE 6\n" +
                            "Ajoutez alors le thym et le laurier, mouillez de 50 cl d'eau, rajoutez les herbes fraîches hachées, 2 morceaux de sucre, la harissa, assaisonner.\n" +
                            "\n" +
                            "ÉTAPE 7\n" +
                            "Laissez mijoter 30 min à feu doux.\n" +
                            "\n" +
                            "ÉTAPE 8\n" +
                            "10 minutes avant la fin, faites cuire les pâtes al dente.\n" +
                            "\n" +
                            "ÉTAPE 9\n" +
                            "Egouttez-les pâtes, mélangez-les avec un peu de beurre.\n" +
                            "\n" +
                            "ÉTAPE 10\n" +
                            "Retirez le laurier de la sauce, versez-la sur les pâtes.\n" +
                            "\n" +
                            "ÉTAPE 11\n" +
                            "Parsemez de parmesan et servez.",
                    "Ma recette maison de bolognaise",
                    bolognese, bolognese, 6);

            int bologneseId = dbHelper.recipes_GetIdByName("Spaghettis bolognaise");
            ArrayList<String> bologneseIngre = new ArrayList<>();
            bologneseIngre.add("sel");
            bologneseIngre.add("poivre");
            bologneseIngre.add("sucre");
            bologneseIngre.add("parmesan");
            bologneseIngre.add("1 pointe de harissa");
            bologneseIngre.add("thym");
            bologneseIngre.add("1 branche de basilic");
            bologneseIngre.add("5 branches de persil");
            bologneseIngre.add("1 branche de sauge");
            bologneseIngre.add("1 branche de céleri");
            bologneseIngre.add("ail : 3 gousses");
            bologneseIngre.add("huile olive");
            bologneseIngre.add("8 champignons de paris");
            bologneseIngre.add("1 petite boite de concentré de tomates");
            bologneseIngre.add("200g de sauce tomate");
            bologneseIngre.add("500g de spaghettis");
            bologneseIngre.add("400 de boeuf haché");
            bologneseIngre.add("3 oignons");
            bologneseIngre.add("laurier");


            for (int i = 0; i < bologneseIngre.size(); i++)
            {
                dbHelper.ingredients_Insert(bologneseId,bologneseIngre.get(i));
            }





        }

        TextView resultTextView = (TextView) view.findViewById(R.id.txt_DBresult);

        newList = dbHelper.recipes_SelectNew();

        newGridView.setAdapter(new MainRecipeAdapter(this.getContext(), newList, R.layout.fragment_home_recipeitem));

        newGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                RecipeItem selectRecipe = newList.get(position);
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("recipe", selectRecipe.get_recipeName());
                startActivity(intent);
                //Toast.makeText(view.getContext(),selectRecipe.get_recipeName(),Toast.LENGTH_SHORT).show();
            }
        });


        //connect GrieView code to UI
        GridView bestGridView = view.findViewById(R.id.GridView_Best);

        bestList = dbHelper.recipes_SelectBest();

        bestGridView.setAdapter(new MainRecipeAdapter(this.getContext(), bestList, R.layout.fragment_home_recipeitem));

        bestGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                RecipeItem selectRecipe = bestList.get(position);
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("recipe", selectRecipe.get_recipeName());
                startActivity(intent);
                //Toast.makeText(view.getContext(),selectRecipe.get_recipName(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
