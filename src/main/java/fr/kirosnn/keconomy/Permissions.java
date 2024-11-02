package fr.kirosnn.keconomy;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.permission.PermissionComponent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public final class Permissions extends PermissionComponent {
    public static final Permission BALANCE = new Permission("keconomy.balance", PermissionDefault.TRUE);
    public static final Permission BALANCE_OTHERS = new Permission("keconomy.balance.others", PermissionDefault.TRUE);
    public static final Permission BALANCE_TOP = new Permission("keconomy.balancetop", PermissionDefault.TRUE);
    public static final Permission PAY = new Permission("keconomy.pay", PermissionDefault.TRUE);
    public static final Permission ADMIN = new Permission("keconomy.set", PermissionDefault.OP);
    public static final Permission RELOAD = new Permission("keconomy.reload", PermissionDefault.OP);

    public Permissions(BasePlugin plugin) {
        super(plugin);
    }
}
