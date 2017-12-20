package com.matheus.magazinehenrique.slidingTabUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.matheus.magazinehenrique.fragment.CategoriasFragment;
import com.matheus.magazinehenrique.fragment.ProdutosFragment;

/**
 * Created by matheus on 01/05/17.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"Categorias", "Produtos"};

    public TabAdapter(FragmentManager fm){
        super(fm);
    }

    private Fragment mCurrentFragment;

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new CategoriasFragment();
                break;
            case 1:
                fragment = new ProdutosFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
