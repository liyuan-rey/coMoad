// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MergeSort.java

package dyna.uic;


public abstract class MergeSort
{

    public MergeSort()
    {
    }

    public void sort(Object array[])
    {
        if(array != null && array.length > 1)
        {
            int maxLength = array.length;
            swapSpace = new Object[maxLength];
            toSort = array;
            mergeSort(0, maxLength - 1);
            swapSpace = null;
            toSort = null;
        }
    }

    public abstract int compareElementsAt(int i, int j);

    protected void mergeSort(int begin, int end)
    {
        if(begin != end)
        {
            int mid = (begin + end) / 2;
            mergeSort(begin, mid);
            mergeSort(mid + 1, end);
            merge(begin, mid, end);
        }
    }

    protected void merge(int begin, int middle, int end)
    {
        int count;
        int firstHalf = count = begin;
        int secondHalf;
        for(secondHalf = middle + 1; firstHalf <= middle && secondHalf <= end;)
            if(compareElementsAt(secondHalf, firstHalf) < 0)
                swapSpace[count++] = toSort[secondHalf++];
            else
                swapSpace[count++] = toSort[firstHalf++];

        if(firstHalf <= middle)
            while(firstHalf <= middle) 
                swapSpace[count++] = toSort[firstHalf++];
        else
            while(secondHalf <= end) 
                swapSpace[count++] = toSort[secondHalf++];
        for(count = begin; count <= end; count++)
            toSort[count] = swapSpace[count];

    }

    protected Object toSort[];
    protected Object swapSpace[];
}
