package de.hftl.levast.password;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




import java.util.ArrayList;
import java.util.List;


/**
 * Created by Levast on 05.02.2019.
 */

public class CustomView extends ArrayAdapter<Integer> {
    private ArrayList<ImageLegend> img;
    private Context context;

    private final int marginBetweenPictures =10;//pixel
    private static final String TAG ="CustomVIew";
    private int sizeImg;

    public CustomView(@NonNull Context context, List<ImageLegend> img) {
        super(context,0, ImageLegend.getOnlyIdFrom(img));//AppDataBase.getDataBase(context).imageLegendDao().getAllIdFromTheme(ImageLegend.SUBJECT));
        this.img=new ArrayList<>(img);
        this.context= context;
        sizeImg=0;
    }



    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        //we create our viewHolder to save our view
        ViewHolder viewHolder=new ViewHolder();
        //Log.d(TAG,"Image counter: " + position + " ConvertView: " + convertView);
        //if we are at the beginning convertView is null , we have to inflate it
        if(listItem == null)
        {
            listItem = LayoutInflater.from(context).inflate(R.layout.layoutadapter,parent,false);
            ImageView imageView=listItem.findViewById(R.id.imageView);
            TextView textLegend=listItem.findViewById(R.id.textLegend);

            sizeImg=(MainActivity.getScreenWidth()-2*marginBetweenPictures*MainActivity.NBR_COLUMN)/MainActivity.NBR_COLUMN;
            LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(sizeImg,sizeImg);
            param.setMargins(marginBetweenPictures, marginBetweenPictures, marginBetweenPictures, marginBetweenPictures);
            imageView.setLayoutParams(param);

            viewHolder.setImageView(imageView);
            viewHolder.setTextLegend(textLegend);
            listItem.setTag(viewHolder);
        }else
        {
            //we get the views previously registered
            viewHolder= (ViewHolder) listItem.getTag();
        }

        //we get the currently image selected
        ImageLegend currentImg=img.get(position);

        if(viewHolder.getImageView().getDrawable()!=null)
            ((BitmapDrawable)viewHolder.getImageView().getDrawable()).getBitmap().recycle();

        //viewHolder.getImageView().setImageResource(currentImg);
        viewHolder.getImageView().setImageBitmap(decodeSampledBitmapFromResource(this.getContext().getResources(),currentImg.getIdImage(),sizeImg,sizeImg));
        viewHolder.getTextLegend().setText(currentImg.getLegend() );

        return listItem;
    }

    public void setImg(List<ImageLegend> img) {
        this.img.clear();
        this.img.addAll(img);
    }

    // class to save view
    public class ViewHolder{
        private ImageView imageView;
        private TextView textLegend;

        public TextView getTextLegend() {
            return textLegend;
        }

        public void setTextLegend(TextView textLegend) {
            this.textLegend = textLegend;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }
}
