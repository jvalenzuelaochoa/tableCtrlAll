package project.tableCtrlManual.TreeTable;

import project.tableCtrlManual.UsrCtrl.User;

import java.util.HashMap;

public class TreeRow {

    private User usr;
    HashMap<User.userAttributes, TreeRow> left;
    HashMap<User.userAttributes, TreeRow> right;
    HashMap<User.userAttributes, TreeRow> parent;

    public TreeRow(User usr)
    {
        this.usr = usr;

        left = new HashMap<User.userAttributes, TreeRow>();
        right = new HashMap<User.userAttributes, TreeRow>();
        parent = new HashMap<User.userAttributes, TreeRow>();
        for(User.userAttributes att: User.userAttributes.values())
        {
            left.put(att, null);
            right.put(att, null);
            parent.put(att, null);
        }
    }

    public TreeRow getLeft(User.userAttributes att)
    {
        return left.get(att);
    }

    public TreeRow getRight(User.userAttributes att)
    {
        return right.get(att);
    }

    public TreeRow getParent(User.userAttributes att)
    {
        return parent.get(att);
    }

    public User getUsr() {
        return usr;
    }

    public void setLeft(User.userAttributes att, TreeRow newLeft)
    {
        this.left.replace(att, newLeft);
    }

    public void setRight(User.userAttributes att, TreeRow newRight)
    {
        this.right.replace(att, newRight);
    }

    public void setParent(User.userAttributes att, TreeRow newParent)
    {
        this.parent.replace(att, newParent);
    }

    public void setUsr(User usr) {
        this.usr = usr;
    }
}
