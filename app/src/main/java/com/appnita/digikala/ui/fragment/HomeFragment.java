package com.appnita.digikala.ui.fragment;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appnita.digikala.R;
import com.appnita.digikala.databinding.FragmentHomeBinding;
import com.appnita.digikala.retrofit.RecyclerAdapterClass;
import com.appnita.digikala.retrofit.RecyclerAdapterConsult;
import com.appnita.digikala.retrofit.RecyclerAdapterNews;
import com.appnita.digikala.retrofit.RecyclerObjectClass;
import com.appnita.digikala.retrofit.basket.AdapterProducts;
import com.appnita.digikala.retrofit.retrofit.ApiService;
import com.appnita.digikala.retrofit.retrofit.NewsRetrofit;
import com.appnita.digikala.retrofit.retrofit.RetrofitSetting;
import com.appnita.digikala.retrofit.room.PostsDao;
import com.appnita.digikala.retrofit.room.PostsDatabase;
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private Handler handler;
    private Runnable runnable;
    RecyclerViewSkeletonScreen skeletonScreen1, skeletonScreen2, skeletonScreen3;


    PostsDao postsDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);

        //timer for event
        countDownStart();

        //skeleton
        RecyclerAdapterNews adapter = new RecyclerAdapterNews();
        skeletonScreen1 = Skeleton.bind(binding.rvNews)
                .adapter(adapter)
                .duration(2000)
                .load(R.layout.item_skeleton)
                .show();

        RecyclerAdapterConsult adapter2 = new RecyclerAdapterConsult();
        skeletonScreen2 = Skeleton.bind(binding.rvConsult)
                .adapter(adapter2)
                .duration(2000)
                .load(R.layout.item_skeleton)
                .show();

        RecyclerAdapterClass adapter3 = new RecyclerAdapterClass();
        skeletonScreen3 = Skeleton.bind(binding.rvClass)
                .adapter(adapter3)
                .duration(2000)
                .load(R.layout.item_skeleton)
                .show();

        //Room
        postsDao = PostsDatabase.getDatabase(getContext()).postsDao();

        RecyclerObjectClass rvOBJ = new RecyclerObjectClass();
        rvOBJ.setTitle("arash");
        rvOBJ.setContent("arash2");
        rvOBJ.setImage("salam");
//        postsDao.insert(rvOBJ);

        //retrofit setting
//        RetrofitConfigurationNews();
//        RetrofitConfigurationConsult();
//        RetrofitConfigurationClass();

        RetrofitConfiguration();
        return binding.getRoot();
    }

    private void RetrofitConfiguration() {
        //server config
        RetrofitSetting retrofit = new RetrofitSetting("https://www.group2080.ir/api/");
        ApiService apiService = retrofit.getApiService();


        Call<NewsRetrofit> call = apiService.news();
        call.enqueue(new Callback<NewsRetrofit>() {
            @Override
            public void onResponse(Call<NewsRetrofit> call, Response<NewsRetrofit> response) {
                if (response.isSuccessful()) {
                    List<NewsRetrofit.posts> list;
                    list = response.body().getNews();


                    if (postsDao.getAllPosts().size() == 0) {
                        for (int i = list.size() - 1; i >= 0; i--) {//
                            RecyclerObjectClass rvOBJ = new RecyclerObjectClass();
                            rvOBJ.setTitle(list.get(i).getTitle());
                            rvOBJ.setContent(list.get(i).getContent());
                            rvOBJ.setImage(list.get(i).getThumbnail());
                            rvOBJ.setUrl(list.get(i).getUrl());
                            rvOBJ.setCategory(list.get(i).getCategories().get(0).getId());
                            postsDao.insert(rvOBJ);
                        }

                        if (list.get(list.size() - 1).getTitle().equals
                                (postsDao.getAllPosts().get(postsDao.getAllPosts().size() - 1).getTitle())) {

                            for (int i = list.size() - 1; i >= 0; i--) {
                                RecyclerObjectClass rvOBJ = new RecyclerObjectClass();
                                rvOBJ.setTitle(list.get(i).getTitle());
                                rvOBJ.setContent(list.get(i).getContent());
                                rvOBJ.setImage(list.get(i).getThumbnail());
                                rvOBJ.setUrl(list.get(i).getUrl());
                                rvOBJ.setCategory(list.get(i).getCategories().get(0).getId());
                                postsDao.insert(rvOBJ);
                            }
                        }

                        skeletonScreen3.hide();
                        //recycler view
                        RecyclerViewConfiqurationNews(postsDao.getCategoraizedPosts(393));
                        RecyclerViewConfiqurationConsult(postsDao.getCategoraizedPosts(388));
                        RecyclerViewConfiqurationClass(postsDao.getCategoraizedPosts(394));
                    } else {
                        skeletonScreen3.hide();
                        RecyclerViewConfiqurationNews(postsDao.getCategoraizedPosts(393));
                        RecyclerViewConfiqurationConsult(postsDao.getCategoraizedPosts(388));
                        RecyclerViewConfiqurationClass(postsDao.getCategoraizedPosts(394));
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsRetrofit> call, Throwable t) {
                skeletonScreen3.hide();
                RecyclerViewConfiqurationNews(postsDao.getCategoraizedPosts(393));
                RecyclerViewConfiqurationConsult(postsDao.getCategoraizedPosts(388));
                RecyclerViewConfiqurationClass(postsDao.getCategoraizedPosts(394));
            }

        });
    }

    private void RecyclerViewConfiqurationNews(List<RecyclerObjectClass> list) {
        RecyclerAdapterNews adapter = new RecyclerAdapterNews(getContext(), list);
        binding.rvNews.setAdapter(adapter);
    }

    private void RecyclerViewConfiqurationConsult(List<RecyclerObjectClass> list) {
        RecyclerAdapterConsult adapter = new RecyclerAdapterConsult(getContext(), list);
        binding.rvConsult.setAdapter(adapter);
    }

    private void RecyclerViewConfiqurationClass(List<RecyclerObjectClass> list) {
        RecyclerAdapterClass adapter = new RecyclerAdapterClass(getContext(), list);
        binding.rvClass.setAdapter(adapter);
    }

    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    // Please here set your event date//YYYY-MM-DD
                    Date futureDate = dateFormat.parse("2021-6-2");
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime() + (8 * 60 * 60 * 1000)
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        binding.txtTimerDay.setText("" + String.format("%02d", days));
                        binding.txtTimerHour.setText("" + String.format("%02d", hours));
                        binding.txtTimerMinute.setText("" + String.format("%02d", minutes));
                        binding.txtTimerSecond.setText("" + String.format("%02d", seconds));
                    } else {
                        Toast.makeText(getContext(), "کنکور دادی رفت !", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }


}