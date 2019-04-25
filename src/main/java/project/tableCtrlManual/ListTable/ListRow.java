package project.tableCtrlManual.ListTable;

import project.tableCtrlManual.UsrCtrl.User;

import java.util.HashMap;

public class ListRow {

    private User usr;
    //TODO: consider adding previous.
    HashMap<User.userAttributes, ListRow> next;
    HashMap<User.userAttributes, ListRow> previous;

    public ListRow()
    {
        usr = null;
        next = new HashMap<User.userAttributes, ListRow>();
        previous = new HashMap<User.userAttributes, ListRow>();
        for(User.userAttributes att: User.userAttributes.values())
        {
            next.put(att, null);
            previous.put(att, null);
        }
    }

    public void setUsr(User usr) {
        this.usr = usr;
    }

    public User getUsr() {
        return usr;
    }

    public ListRow getNext(User.userAttributes att)
    {
        return next.get(att);
    }

    public ListRow getPrevious(User.userAttributes att)
    {
        return previous.get(att);
    }

    public void setPrevious(User.userAttributes att, ListRow prevColumn)
    {
        this.previous.replace(att, prevColumn);
    }

    public void setNext(User.userAttributes att, ListRow nextColumn)
    {
        this.next.replace(att, nextColumn);
    }
}
