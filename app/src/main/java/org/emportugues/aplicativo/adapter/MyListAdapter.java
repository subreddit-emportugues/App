package org.emportugues.aplicativo.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;

import org.emportugues.aplicativo.R;
import org.emportugues.aplicativo.model.Subreddit;
import org.emportugues.aplicativo.model.SubredditList;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyListAdapter extends ArrayAdapter<Subreddit> {

    private List<Subreddit> subreddits;
    private Context context;
    private ArrayList<Subreddit> originalList;
    private LayoutInflater mInflater;
    private SubredditList subredditList;
    private CustomFilter mFliter;

    // Constructors
    public MyListAdapter(Context context, SubredditList subredditList, String sortingColumn, boolean reverse) {
        super(context, 0, subredditList.getSubreddits(sortingColumn, reverse));
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.subredditList = subredditList;
        subreddits = subredditList.getSubreddits(sortingColumn, reverse);
        originalList = new ArrayList<>(subreddits);
    }

    private static String getDate(String stringData) throws ParseException {
        SimpleDateFormat inputDate = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = inputDate.parse(stringData);
        SimpleDateFormat outputDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        assert date != null;
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
            View view = mInflater.inflate(R.layout.adapter_row, parent, false);
            viewHolder = ViewHolder.create((LinearLayout) view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Subreddit item = getItem(position);

        assert item != null;
        if (item.getIcon().equals("")) { //url.isEmpty()
            GlideApp.with(context)
                    .load(context.getString(R.string.url_subreddit_icon))
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(viewHolder.imageViewIcon);
        } else {
            GlideApp.with(context)
                    .load(item.getIcon())
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
        viewHolder.textViewModerators.setText(String.valueOf(item.getModerators().size()));
        if (item.getNSFW()) {
            viewHolder.imageViewNSFW.setImageResource(R.drawable.ic_check_box_black_24dp);
        } else {
            viewHolder.imageViewNSFW.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }

        if (position % 2 == 1) {
            viewHolder.frameIcon.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray500));
            viewHolder.textViewName.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray500));
            viewHolder.textViewDescription.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray500));
            viewHolder.textViewMembers.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray500));
            viewHolder.textViewAge.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray500));
            viewHolder.frameNSFW.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray500));
            viewHolder.textViewActivity.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray500));
            viewHolder.textViewModerators.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray500));
        } else {
            viewHolder.frameIcon.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray400));
            viewHolder.textViewName.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray400));
            viewHolder.textViewDescription.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray400));
            viewHolder.textViewMembers.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray400));
            viewHolder.textViewAge.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray400));
            viewHolder.frameNSFW.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray400));
            viewHolder.textViewActivity.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray400));
            viewHolder.textViewModerators.setBackgroundColor(getContext().getResources().getColor(R.color.colorBlueGray400));
        }

        return viewHolder.rootView;

    }

    public Filter getFilter() {
        if (mFliter == null)
            mFliter = new CustomFilter();
        return mFliter;

    }

    private static class ViewHolder {
        final LinearLayout rootView;
        final FrameLayout frameIcon;
        final ImageView imageViewIcon;
        final TextView textViewName;
        final TextView textViewDescription;
        final TextView textViewActivity;
        final TextView textViewMembers;
        final TextView textViewAge;
        final TextView textViewModerators;
        final FrameLayout frameNSFW;
        final ImageView imageViewNSFW;

        private ViewHolder(LinearLayout rootView,
                           FrameLayout frameIcon,
                           ImageView imageViewIcon,
                           TextView textViewName,
                           TextView textViewDescription,
                           TextView textViewActivity,
                           TextView textViewMembers,
                           TextView textViewAge,
                           TextView textViewModerators,
                           FrameLayout frameNSFW,
                           ImageView imageViewNSFW
        ) {
            this.rootView = rootView;
            this.frameIcon = frameIcon;
            this.imageViewIcon = imageViewIcon;
            this.textViewName = textViewName;
            this.textViewDescription = textViewDescription;
            this.textViewActivity = textViewActivity;
            this.textViewMembers = textViewMembers;
            this.textViewAge = textViewAge;
            this.textViewModerators = textViewModerators;
            this.frameNSFW = frameNSFW;
            this.imageViewNSFW = imageViewNSFW;
        }

        static ViewHolder create(LinearLayout rootView) {
            FrameLayout frameIcon = rootView.findViewById(R.id.frameIcon);
            ImageView imageViewIcon = rootView.findViewById(R.id.imageViewIcon);
            TextView textViewName = rootView.findViewById(R.id.textViewName);
            TextView textViewDescription = rootView.findViewById(R.id.textViewDescription);
            TextView textViewActivity = rootView.findViewById(R.id.textViewActivity);
            TextView textViewMembers = rootView.findViewById(R.id.textViewMembers);
            TextView textViewAge = rootView.findViewById(R.id.textViewAge);
            TextView textViewModerators = rootView.findViewById(R.id.textViewModerators);
            FrameLayout frameNSFW = rootView.findViewById(R.id.frameNSFW);
            ImageView imageViewNSFW = rootView.findViewById(R.id.imageViewNSFW);

            return new ViewHolder(rootView,
                    frameIcon,
                    imageViewIcon,
                    textViewName,
                    textViewDescription,
                    textViewActivity,
                    textViewMembers,
                    textViewAge,
                    textViewModerators,
                    frameNSFW,
                    imageViewNSFW
            );
        }

    }

    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                result.values = originalList;
                result.count = originalList.size();
            } else {
                final List<Subreddit> filteredList = new ArrayList<>();
                String lowerConstraint = constraint.toString().toLowerCase();
                for (Subreddit subList : originalList) {
                    final String value =
                            subList.getName().toLowerCase() +
                                    subList.getDescription().toLowerCase() +
                                    subList.getSubmissions() +
                                    subList.getComments() +
                                    subList.getMembers() +
                                    subList.getModerators().toString().replace("[", "").replace("]", "").toLowerCase();
                    if (value.contains(lowerConstraint)) {
                        filteredList.add(subList);
                    }
                }
                result.values = filteredList;
                result.count = filteredList.size();
            }

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.count < 1) {
                Toast emptyToast = Toast.makeText(context, R.string.toast_empty, Toast.LENGTH_SHORT);
                emptyToast.getView().setBackgroundColor(context.getResources().getColor(R.color.colorOrangeA400));
                emptyToast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastLayout = (LinearLayout) emptyToast.getView();
                TextView toastTextView = (TextView) toastLayout.getChildAt(0);
                toastTextView.setTextSize(context.getResources().getDimension(R.dimen._4ssp));
                toastTextView.setTextColor(context.getResources().getColor(R.color.colorBlueGray900));
                emptyToast.show();
            }

            ArrayList<Subreddit> filteredSubreddits = (ArrayList<Subreddit>) results.values;
            clear();
            addAll(filteredSubreddits);
        }
    }

}
