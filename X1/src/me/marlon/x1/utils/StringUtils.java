package me.marlon.x1.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Arrays;

public class StringUtils {

    public static Location stringToLocation(String str) {
        try {
            if (str == null)
                return null;


            str = str.replace("[", "");
            str = str.replace("]", "");
            String[] list = str.split(",");

            return new Location(Bukkit.getWorld(list[0]), Float.parseFloat(list[1]), Float.parseFloat(list[2]), Float.parseFloat(list[3]), Float.parseFloat(list[4]), Float.parseFloat(list[5]));
        } catch (Exception e) {
            return null;
        }
    }

    public static String locationToString(Location location) {
        try {
            if (location == null)
                return null;
            String world = location.getWorld().getName();
            String x = location.getX() + "";
            String y = location.getY() + "";
            String z = location.getZ() + "";
            String yaw = location.getYaw() + "";
            String pitch = location.getPitch() + "";

            String[] loc = {world, x, y, z, yaw, pitch};
            return Arrays.toString(loc);
        }catch (Exception e) {
            return null;
        }
    }
}
