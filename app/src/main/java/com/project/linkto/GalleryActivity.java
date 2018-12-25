package com.project.linkto;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.project.linkto.utils.Utils;


public class GalleryActivity extends Activity {

    public static final int LOADER_GALLERY = 0x1;
    public static String currentImage="";
    private ImageView close_button;
    public static int mSelectedImagePosition = 0;
    private SliderLayout sliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        close_button = (ImageView) findViewById(R.id.close_button);
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        close_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        drawSlider();

 //       Log.i("GalleryGallery 1", currentImage.toString());
    }

    private void drawSlider() {
        if (!Utils.isEmptyString(currentImage)) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView
                    .image(currentImage)

                  //  .setScaleType(BaseSliderView.ScaleType.CenterInside)
            ;


            sliderLayout.addSlider(textSliderView);

        }
/*        if (mImageGalleryList != null && mImageGalleryList.size() != 0) {

            for (PhotosRetrofit pr : mImageGalleryList) {
                String url = pr.getUrl();

                if (!Utils.isEmptyString(url) && !url.equals(currentImage)) {
                    DefaultSliderView textSliderView = new DefaultSliderView(this);
                    textSliderView
                            .image(url)
                            .description(pr.getName())
                            .setScaleType(BaseSliderView.ScaleType.CenterInside);


                    sliderLayout.addSlider(textSliderView);

                    Log.d("GalleryGallery 1", url.toString());
                }
            }

            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);

        }*/
    }


}
