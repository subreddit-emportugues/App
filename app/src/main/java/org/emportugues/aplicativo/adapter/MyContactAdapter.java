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

import com.squareup.picasso.Picasso;

import org.emportugues.aplicativo.R;
import org.emportugues.aplicativo.model.Contact;
import org.emportugues.aplicativo.model.ContactList;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyContactAdapter extends ArrayAdapter<Contact> {

    List<Contact> contacts;
    Context context;
    private LayoutInflater mInflater;
    private ContactList contactList;

    // Constructors
    public MyContactAdapter(Context context, ContactList contactList) {
        super(context, 0, contactList.getContacts());
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.contactList = contactList;
        contacts = contactList.getContacts();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
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

        Contact item = getItem(position);

        viewHolder.textViewName.setText(item.getName());
        viewHolder.textViewDescription.setText(item.getDescription());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String decimalFormat = formatter.format(item.getMembers()).replace(",", ".");
        viewHolder.textViewMembers.setText(decimalFormat);
        try {
            viewHolder.textViewAge.setText(getDate(new Date(item.getAge() * 1000).toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (contacts.get(position).getNSFW()) {
            viewHolder.textViewNSFW.setText(R.string.nsfw_true);
        } else {
            viewHolder.textViewNSFW.setText(R.string.nsfw_false);
        }
        viewHolder.textViewActivity.setText(String.valueOf(item.getTotalActivity(contactList)));
        viewHolder.textViewModerators.setText(String.valueOf(item.getModerators().length));
        if (contacts.get(position).getIcon().equals("")) { //url.isEmpty()
            Picasso.get()
                    .load("https://b.thumbs.redditmedia.com/hiWgtDrja9SM3iHyv_b3dtES28ZBAKTBeJCfrZ03mNM.jpg")
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(viewHolder.imageViewIcon);
        } else {
            Picasso.get()
                    .load(contacts.get(position).getIcon())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(viewHolder.imageViewIcon); //this is your ImageView
        }

        if (contacts.get(position).getId() % 2 == 1) {
            viewHolder.frameLayout.setBackgroundColor(getContext().getResources().getColor(R.color.indigo500));
            viewHolder.textViewName.setBackgroundColor(getContext().getResources().getColor(R.color.indigo500));
            viewHolder.textViewDescription.setBackgroundColor(getContext().getResources().getColor(R.color.indigo500));
            viewHolder.textViewMembers.setBackgroundColor(getContext().getResources().getColor(R.color.indigo500));
            viewHolder.textViewAge.setBackgroundColor(getContext().getResources().getColor(R.color.indigo500));
            viewHolder.textViewNSFW.setBackgroundColor(getContext().getResources().getColor(R.color.indigo500));
            viewHolder.textViewActivity.setBackgroundColor(getContext().getResources().getColor(R.color.indigo500));
            viewHolder.textViewModerators.setBackgroundColor(getContext().getResources().getColor(R.color.indigo500));
        } else {
            viewHolder.frameLayout.setBackgroundColor(getContext().getResources().getColor(R.color.indigo400));
            viewHolder.textViewName.setBackgroundColor(getContext().getResources().getColor(R.color.indigo400));
            viewHolder.textViewDescription.setBackgroundColor(getContext().getResources().getColor(R.color.indigo400));
            viewHolder.textViewMembers.setBackgroundColor(getContext().getResources().getColor(R.color.indigo400));
            viewHolder.textViewAge.setBackgroundColor(getContext().getResources().getColor(R.color.indigo400));
            viewHolder.textViewNSFW.setBackgroundColor(getContext().getResources().getColor(R.color.indigo400));
            viewHolder.textViewActivity.setBackgroundColor(getContext().getResources().getColor(R.color.indigo400));
            viewHolder.textViewModerators.setBackgroundColor(getContext().getResources().getColor(R.color.indigo400));
        }

        return viewHolder.rootView;

    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final FrameLayout frameLayout;
        public final ImageView imageViewIcon;
        public final TextView textViewName;
        public final TextView textViewDescription;
        public final TextView textViewMembers;
        public final TextView textViewAge;
        public final TextView textViewNSFW;
        public final TextView textViewActivity;
        public final TextView textViewModerators;

        private ViewHolder(LinearLayout rootView,
                           FrameLayout frameLayout,
                           ImageView imageViewIcon,
                           TextView textViewName,
                           TextView textViewDescription,
                           TextView textViewMembers,
                           TextView textViewAge,
                           TextView textViewNSFW,
                           TextView textViewActivity,
                           TextView textViewModerators
        ) {
            this.rootView = rootView;
            this.frameLayout = frameLayout;
            this.imageViewIcon = imageViewIcon;
            this.textViewName = textViewName;
            this.textViewDescription = textViewDescription;
            this.textViewMembers = textViewMembers;
            this.textViewAge = textViewAge;
            this.textViewNSFW = textViewNSFW;
            this.textViewActivity = textViewActivity;
            this.textViewModerators = textViewModerators;
        }

        public static ViewHolder create(LinearLayout rootView) {
            FrameLayout frameLayout = rootView.findViewById(R.id.frameLayout);
            ImageView imageViewIcon = rootView.findViewById(R.id.imageViewIcon);
            TextView textViewName = rootView.findViewById(R.id.textViewName);
            TextView textViewDescription = rootView.findViewById(R.id.textViewDescription);
            TextView textViewMembers = rootView.findViewById(R.id.textViewMembers);
            TextView textViewAge = rootView.findViewById(R.id.textViewAge);
            TextView textViewNSFW = rootView.findViewById(R.id.textViewNSFW);
            TextView textViewSubmissions = rootView.findViewById(R.id.textViewActivity);
            TextView textViewModerators = rootView.findViewById(R.id.textViewModerators);
            return new ViewHolder(rootView,
                    frameLayout,
                    imageViewIcon,
                    textViewName,
                    textViewDescription,
                    textViewMembers,
                    textViewAge,
                    textViewNSFW,
                    textViewSubmissions,
                    textViewModerators
            );
        }

    }

    private static String getDate(String stringData) throws ParseException {
        SimpleDateFormat inputDate = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.getDefault());
        Date date = inputDate.parse(stringData);
        SimpleDateFormat outputDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        System.out.println(date);
        return outputDate.format(date);
    }

}
