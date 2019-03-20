package org.emportugues.aplicativo.ui.tableview.holder;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import org.emportugues.aplicativo.R;
import org.emportugues.aplicativo.ui.tableview.model.CellModel;

public class NSFWCellViewHolder extends AbstractViewHolder {

    private final ImageButton cell_image_button;

    private final Drawable icon_default;
    private final Drawable icon_nsfw;

    public NSFWCellViewHolder(View itemView) {
        super(itemView);
        cell_image_button = itemView.findViewById(R.id.cell_image_button);

        // Get vector drawables
        icon_default = ContextCompat.getDrawable(itemView.getContext(), R.drawable.subreddit);
        icon_nsfw = ContextCompat.getDrawable(itemView.getContext(), R.drawable.subreddit_nsfw);
    }

    public void setCellModel(CellModel p_jModel) {
        char c = String.valueOf(p_jModel.getData()).trim().charAt(0);

        if (c == 't') {
            cell_image_button.setImageDrawable(icon_nsfw);
        } else if (c == 'f') {
            cell_image_button.setImageDrawable(icon_default);
        }
    }

}
