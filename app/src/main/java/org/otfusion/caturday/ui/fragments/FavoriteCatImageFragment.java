package org.otfusion.caturday.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import org.otfusion.caturday.R;
import org.otfusion.caturday.common.model.Cat;
import org.otfusion.caturday.util.ApplicationUtils;
import org.otfusion.caturday.util.FileUtils;

import butterknife.BindView;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class FavoriteCatImageFragment extends BaseFragment {

    public static final String CAT_KEY = "cat";
    @BindView(R.id.favorite_cat)
    ImageViewTouch mImageViewTouch;

    public static FavoriteCatImageFragment newInstance(Cat cat) {
        FavoriteCatImageFragment fragment = new FavoriteCatImageFragment();
        Bundle args = new Bundle();

        // if the object starts to grow, lets use Parcelable.
        args.putSerializable("cat", cat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_favorite_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite_menu_share:
                Cat cat = getCatFromArguments();
                String filePath = FileUtils.getFileName(cat, true);
                Uri fileUri;
                if (filePath.isEmpty()) {
                    fileUri = ApplicationUtils.getLocalBitmapUri(mImageViewTouch);
                } else {
                    fileUri = Uri.parse(filePath);
                }
                Intent shareImageIntent = ApplicationUtils.getShareImageIntent(fileUri);
                startActivity(Intent.createChooser(shareImageIntent, "Share a cat!"));
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_favorite_cat_image;
    }

    @Override
    public void loadUIContent() {
        Cat cat = getCatFromArguments();
        mImageViewTouch.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        String filePath = FileUtils.getFileName(cat, true);
        if (filePath.isEmpty()) {
            Picasso.with(getActivity()).load(cat.getImageUrl()).into(mImageViewTouch);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            mImageViewTouch.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getTitleId() {
        return R.string.title_activity_favorite;
    }

    private Cat getCatFromArguments() {
        return (Cat) getArguments().getSerializable(CAT_KEY);
    }
}
