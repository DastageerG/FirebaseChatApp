package com.example.firebasechatapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.firebasechatapp.fragments.ChatFragment;
import com.example.firebasechatapp.fragments.FriendsFragment;
import com.example.firebasechatapp.fragments.RequestsFragment;

public class SectionsPageAdapter extends FragmentPagerAdapter
{

    public SectionsPageAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;
            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
               return "Requests";
            case 1:
                return "Chat";
            case 2:
               return  "Friends";
            default:
                return null;
        }
    }
}
