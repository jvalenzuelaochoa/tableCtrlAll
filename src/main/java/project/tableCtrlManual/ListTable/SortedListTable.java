package project.tableCtrlManual.ListTable;

import project.tableCtrlManual.UsrCtrl.User;
import org.junit.Test;

public class SortedListTable extends ListTable {

    public SortedListTable()
    {
        super();
    }

    public void addUser(User newUser)
    {
        //TODO: validate user input
        ListRow newColumn = new ListRow();
        newColumn.setUsr(newUser);

        if (head.get(User.userAttributes.ID)==null)
        {
            for(User.userAttributes att: User.userAttributes.values())
            {
                head.replace(att, newColumn);
                tail.replace(att, newColumn);
            }
        }
        else
        {
            for(User.userAttributes att: User.userAttributes.values())
            {
                ListRow traverser = head.get(att);
                while (traverser != null)
                {
                    if(traverser.getUsr().attributeIsGreater(att, newUser))
                    {
                        if (traverser != head.get(att))
                        {
                            traverser.getPrevious(att).setNext(att, newColumn);
                        }
                        else
                            {
                            head.replace(att, newColumn);
                        }
                        newColumn.setPrevious(att, traverser.getPrevious(att));
                        newColumn.setNext(att, traverser);
                        traverser.setPrevious(att, newColumn);

                        break;
                    }
                    traverser = traverser.getNext(att);
                }
                if (traverser == null)
                {
                    tail.get(att).setNext(att, newColumn);
                    newColumn.setPrevious(att, tail.get(att));
                    tail.replace(att, newColumn);
                }
            }
        }

    }

    @Test
    public void quickTest()
    {
        SortedListTable sampleTbl = new SortedListTable();
        sampleTbl.displayByAttribute(User.userAttributes.ID);
        User sampleUsr = new User(1, "Javier", 3500);
        sampleTbl.addUser(sampleUsr);
        sampleTbl.displayByAttribute(User.userAttributes.ID);
    }
    @Test
    public void notSoQuickTest()
    {
        SortedListTable sampleTbl = new SortedListTable();
        sampleTbl.displayByAttribute(User.userAttributes.ID);
        sampleTbl.addUser( new User(1, "Jesus", 3500));
        sampleTbl.addUser( new User(2, "Pedro", 200));
        sampleTbl.addUser( new User(6, "Ochoa", 1000));
        sampleTbl.addUser( new User(5, "Valenzuela", 10000));
        sampleTbl.addUser( new User(3, "Mentira", 7));
        sampleTbl.addUser( new User(4, "Javier", 750));
        sampleTbl.displayByAttribute(User.userAttributes.ID);
        sampleTbl.displayByAttribute(User.userAttributes.NAME);
        sampleTbl.displayByAttribute(User.userAttributes.SALARY);
    }

}
