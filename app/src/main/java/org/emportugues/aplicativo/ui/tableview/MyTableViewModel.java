package org.emportugues.aplicativo.ui.tableview;

import android.view.Gravity;

import org.emportugues.aplicativo.data.database.entity.User;
import org.emportugues.aplicativo.ui.tableview.model.CellModel;
import org.emportugues.aplicativo.ui.tableview.model.ColumnHeaderModel;
import org.emportugues.aplicativo.ui.tableview.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class MyTableViewModel {

    // View Types
    public static final int NSFW_TYPE = 1;

    private List<ColumnHeaderModel> mColumnHeaderModelList;
    private List<RowHeaderModel> mRowHeaderModelList;
    private List<List<CellModel>> mCellModelList;

    public int getCellItemViewType(int column) {

        switch (column) {
            case 4:
                // column 4. is NSFW.
                return NSFW_TYPE;
            default:
                return 0;
        }
    }

    public int getColumnTextAlign(int column) {
        switch (column) {
            // Subreddit
            case 0:
                return Gravity.CENTER;
            // Description
            case 1:
                return Gravity.CENTER;
            // Subscribers
            case 2:
                return Gravity.CENTER;
            // Age
            case 3:
                return Gravity.CENTER;
            // NSFW
            case 4:
                return Gravity.CENTER;
            default:
                return Gravity.CENTER;
        }
    }

    private List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
        list.add(new ColumnHeaderModel("Subreddit"));
        list.add(new ColumnHeaderModel("Descrição"));
        list.add(new ColumnHeaderModel("Subscrições"));
        list.add(new ColumnHeaderModel("Criação"));
        list.add(new ColumnHeaderModel("NSFW"));

        return list;
    }

    private List<List<CellModel>> createCellModelList(List<User> userList) {
        List<List<CellModel>> lists = new ArrayList<>();

        // Creating cell model list from User list for Cell Items
        // In this example, User list is populated from web service
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, user.subreddit)); // "Subreddit"
            list.add(new CellModel("2-" + i, user.description)); // "Description"
            list.add(new CellModel("3-" + i, user.subscribers)); // "Subscribers"
            list.add(new CellModel("4-" + i, user.age)); // "Age"
            list.add(new CellModel("5-" + i, user.nsfw)); // "NSFW"

            // Add
            lists.add(list);
        }

        return lists;
    }

    private List<RowHeaderModel> createRowHeaderList(int size) {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(String.valueOf(i + 1)));
        }
        return list;
    }

    public List<ColumnHeaderModel> getColumHeaderModeList() {
        return mColumnHeaderModelList;
    }

    public List<RowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    public List<List<CellModel>> getCellModelList() {
        return mCellModelList;
    }

    public void generateListForTableView(List<User> users) {
        mColumnHeaderModelList = createColumnHeaderModelList();
        mCellModelList = createCellModelList(users);
        mRowHeaderModelList = createRowHeaderList(users.size());
    }

}
