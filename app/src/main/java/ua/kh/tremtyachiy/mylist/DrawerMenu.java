package ua.kh.tremtyachiy.mylist;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by User on 19.06.2015.
 */
public class DrawerMenu {
    private Drawer drawerMenu;

    public Drawer getDrawerMenu() {
        return drawerMenu;
    }

    public void initDrawerMenu(final Activity activity, Toolbar toolbar){
        drawerMenu = new DrawerBuilder()
//                .withSliderBackgroundColor(activity.getResources().getColor(R.color.blue_main))
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withActivity(activity)
                .addDrawerItems(
                        new SecondaryDrawerItem()
                                .withIcon(R.drawable.ic_settings_white_24dp)
                                .withName(R.string.nav_menu_item1)
                                .withIdentifier(1),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.nav_menu_item2)
                                .withIcon(R.drawable.ic_announcement_white_24dp)
                                .withIdentifier(2),
                        new SecondaryDrawerItem()
                                .withName(R.string.nav_menu_item3)
                                .withIcon(R.drawable.ic_supervisor_account_white_24dp)
                                .withIdentifier(3),
                        new SecondaryDrawerItem()
                                .withName(R.string.nav_menu_item4)
                                .withIcon(R.drawable.ic_live_help_white_24dp)
                                .withIdentifier(4),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.nav_menu_item5)
                                .withIcon(R.drawable.ic_exit_to_app_white_24dp)
                                .withIdentifier(5))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        switch (iDrawerItem.getIdentifier()) {
                            case 5:
                                activity.finish();
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }
}
