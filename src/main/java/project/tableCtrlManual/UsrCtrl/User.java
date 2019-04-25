package project.tableCtrlManual.UsrCtrl;

import java.util.Objects;

public class User {

    public enum userAttributes
    {
        ID, NAME, SALARY;
    }

    public static userAttributes toAttribute(String attStr)
    {
        switch(attStr)
        {
            case "ID": return userAttributes.ID;
            case "NAME" : return userAttributes.NAME;
            case "SALARY" : return userAttributes.SALARY;
            default : throw new IllegalArgumentException("not a supported attribute to convert");
        }
    }

    private Integer id;
    private String name;
    private Integer salary;

    public User(Integer id, String name, Integer salary)
    {
        this.id = id;
        this.name = name;
        this.salary = salary;

    }

    public Object getElement(userAttributes elementName)
    {
        switch (elementName)
        {
            case ID:
            {
                return getId();
            }
            case NAME:
            {
                return  getName();
            }
            case SALARY:
            {
                return getSalary();
            }
        }
        return null;
    }

    public void setElement(userAttributes elementName, Object element )
    {
        switch (elementName)
        {
            case ID:
            {
                if (!(element instanceof Integer))
                {
                    throw new IllegalArgumentException ("Element type mismatch");
                }
                else
                {
                    setId((Integer)element);
                }
            }
            case NAME:
            {
                if (!(element instanceof String))
                {
                    throw new IllegalArgumentException ("Element type mismatch");
                }
                else
                {
                    setName((String)element);
                }
            }
            case SALARY:
            {
                if (!(element instanceof Integer))
                {
                    throw new IllegalArgumentException ("Element type mismatch");
                }
                else
                {
                    setSalary((Integer)element);
                }
            }
        }
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSalary() {
        return salary;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ID: " + id.toString() + "; NAME: " + name + "; SALARY: " + salary.toString();
    }

    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return salary.equals(user.salary) &&
                Objects.equals(name, user.name) &&
                id.equals(user.id);
    }

    public boolean attributeIsSame(userAttributes att, User usr2)
    {
        switch (att)
        {
            case ID:
                return this.getId().equals(usr2.getId());
            case NAME:
                return this.getName().equals(usr2.getName());
            case SALARY:
                return this.getSalary().equals(usr2.getSalary());
            default: throw new IllegalArgumentException("unsupported attribute");
        }
    }

    public boolean attributeIsGreater(userAttributes att, User usr2)
    {
        switch (att)
        {
            case ID:
                return this.getId().compareTo(usr2.getId()) > 0;
            case NAME:
                return this.getName().compareToIgnoreCase(usr2.getName()) > 0;
            case SALARY:
                return this.getSalary().compareTo(usr2.getSalary()) > 0;
            default: throw new IllegalArgumentException("unsupported attribute");
        }

    }

    public boolean attributeIsLess(userAttributes att, User usr2)
    {
        switch (att)
        {
            case ID:
                return this.getId().compareTo(usr2.getId()) < 0;
            case NAME:
                return this.getName().compareToIgnoreCase(usr2.getName()) < 0;
            case SALARY:
                return this.getSalary().compareTo(usr2.getSalary()) < 0;
            default: throw new IllegalArgumentException("unsupported attribute");
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, salary);
    }
}
