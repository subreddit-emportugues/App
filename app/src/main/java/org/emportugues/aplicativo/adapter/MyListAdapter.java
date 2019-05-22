package org.emportugues.aplicativo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import org.emportugues.aplicativo.R;
import org.emportugues.aplicativo.model.Subreddit;
import org.emportugues.aplicativo.model.SubredditList;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyListAdapter extends ArrayAdapter<Subreddit> {

    List<Subreddit> subreddits;
    Context context;
    private LayoutInflater mInflater;
    private SubredditList subredditList;

    // Constructors
    public MyListAdapter(Context context, SubredditList subredditList) {
        super(context, 0, subredditList.getSubreddits());
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.subredditList = subredditList;
        subreddits = subredditList.getSubreddits();
    }

    private static String getDate(String stringData) throws ParseException {
        SimpleDateFormat inputDate = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.getDefault());
        Date date = inputDate.parse(stringData);
        SimpleDateFormat outputDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        System.out.println(date);
        return outputDate.format(date);
    }

    @Override
    public Subreddit getItem(int position) {
        return subreddits.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            viewHolder = ViewHolder.create((LinearLayout) view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Subreddit item = getItem(position);

        if (subreddits.get(position).getIcon().equals("")) { //url.isEmpty()
            GlideApp.with(context)
                    .load(context.getString(R.string.ic_subreddit_icon))
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(viewHolder.imageViewIcon);
        } else {
            GlideApp.with(context)
                    .load(subreddits.get(position).getIcon())
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(viewHolder.imageViewIcon);
        }
        viewHolder.textViewName.setText(item.getName());
        viewHolder.textViewDescription.setText(item.getDescription());
        viewHolder.textViewActivity.setText(String.valueOf(item.getTotalActivity(subredditList)));
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String decimalFormat = formatter.format(item.getMembers()).replace(",", ".");
        viewHolder.textViewMembers.setText(decimalFormat);
        try {
            viewHolder.textViewAge.setText(getDate(new Date(item.getAge() * 1000).toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.textViewModerators.setText(String.valueOf(item.getModerators().length));
        if (subreddits.get(position).getNSFW()) {
            viewHolder.textViewNSFW.setText(R.string.nsfw_true);
        } else {
            viewHolder.textViewNSFW.setText(R.string.nsfw_false);
        }

        return viewHolder.rootView;

    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final FrameLayout frameLayout;
        public final ImageView imageViewIcon;
        public final TextView textViewName;
        public final TextView textViewDescription;
        public final TextView textViewActivity;
        public final TextView textViewMembers;
        public final TextView textViewAge;
        public final TextView textViewModerators;
        public final TextView textViewNSFW;

        private ViewHolder(LinearLayout rootView,
                           FrameLayout frameLayout,
                           ImageView imageViewIcon,
                           TextView textViewName,
                           TextView textViewDescription,
                           TextView textViewActivity,
                           TextView textViewMembers,
                           TextView textViewAge,
                           TextView textViewModerators,
                           TextView textViewNSFW
        ) {
            this.rootView = rootView;
            this.frameLayout = frameLayout;
            this.imageViewIcon = imageViewIcon;
            this.textViewName = textViewName;
            this.textViewDescription = textViewDescription;
            this.textViewActivity = textViewActivity;
            this.textViewMembers = textViewMembers;
            this.textViewAge = textViewAge;
            this.textViewModerators = textViewModerators;
            this.textViewNSFW = textViewNSFW;
        }

        public static ViewHolder create(LinearLayout rootView) {
            FrameLayout frameLayout = rootView.findViewById(R.id.frameLayout);
            ImageView imageViewIcon = rootView.findViewById(R.id.imageViewIcon);
            TextView textViewName = rootView.findViewById(R.id.textViewName);
            TextView textViewDescription = rootView.findViewById(R.id.textViewDescription);
            TextView textViewActivity = rootView.findViewById(R.id.textViewActivity);
            TextView textViewMembers = rootView.findViewById(R.id.textViewMembers);
            TextView textViewAge = rootView.findViewById(R.id.textViewAge);
            TextView textViewModerators = rootView.findViewById(R.id.textViewModerators);
            TextView textViewNSFW = rootView.findViewById(R.id.textViewNSFW);

            return new ViewHolder(rootView,
                    frameLayout,
                    imageViewIcon,
                    textViewName,
                    textViewDescription,
                    textViewActivity,
                    textViewMembers,
                    textViewAge,
                    textViewModerators,
                    textViewNSFW
            );
        }

    }

}
