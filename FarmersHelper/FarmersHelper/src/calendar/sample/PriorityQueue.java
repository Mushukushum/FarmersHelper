package calendar.sample;

import java.util.*;


public class PriorityQueue extends AbstractCollection
{
    private static class DefaultComparator implements Comparator<Event> {
        @Override
        public int compare(Event e, Event e1) {
            return e1.compareTo(e);
        }
    }
    private Comparator<Event> myComp = new DefaultComparator();
//    private Comparator<Event> myComp = new Comparator<Event>() {
//        @Override
//        public int compare(Event event, Event t1) {
//            return 0;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            return false;
//        }
//    };
    private int        mySize;
    private ArrayList<Event>  myList;

    /**
     * This is a trivial iterator class that returns
     * elements in the PriorityQueue ArrayList field
     * one-at-a-time
     */
    private class PQItr implements Iterator
    {
        public Event next()
        {
            return myList.get(myCursor);
        }

        public boolean hasNext()
        {
            return myCursor <= mySize;
        }

        public void remove()
        {
            throw new UnsupportedOperationException("remove not implemented");
        }

        private int myCursor = 1;
    }

    public PriorityQueue()
    {
        myList = new ArrayList<>(32);
        myList.add(null);             // first slot has index 1
        mySize = 0;
    }

    public PriorityQueue(Collection<Event> coll)
    {
        this();
        myList.addAll(coll);
        mySize = coll.size();

        for(int k=coll.size()/2; k >= 1; k--)
        {
            heapify(k);
        }
    }

    public boolean add(Event o)
    {
        myList.add(o);        // stored, but not correct location
        mySize++;             // added element, update count
        int k = mySize;       // location of new element

        while (k > 1 && myComp.compare(myList.get(k/2), o) > 0)
        {
            myList.set(k, myList.get(k/2));
            k /= 2;
        }
        myList.set(k,o);

        return true;
    }

    public int size()
    {
        return mySize;
    }

    public boolean isEmpty()
    {
        return mySize == 0;
    }

    public Event remove()
    {
        if (! isEmpty())
        {
            Event hold = myList.get(1);

            myList.set(1, myList.get(mySize));  // move last to top
            myList.remove(mySize);              // pop last off
            mySize--;
            if (mySize > 1)
            {
                heapify(1);
            }
            return hold;
        }
        return null;
    }

    public Event peek()
    {
        return myList.get(1);
    }

    public Iterator iterator()
    {
        return new PQItr();
    }

    private void heapify(int vroot)
    {
        Event last = myList.get(vroot);
        int child, k = vroot;
        while (2*k <= mySize)
        {
            child = 2*k;
            if (child < mySize &&
                    myComp.compare(myList.get(child),
                            myList.get(child+1)) > 0)
            {
                child++;
            }
            if (myComp.compare(last, myList.get(child)) <= 0)
            {
                break;
            }
            else
            {
                myList.set(k, myList.get(child));
                k = child;
            }
        }
        myList.set(k, last);
    }
}