package com.deikare.tutoring;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

class MinimalHeap {
    private int size = 0;
    private Node [] heap;
    private int currentIndex = 0;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public MinimalHeap(int size) {
        this.size = size;
        this.heap = new Node[size];
        currentIndex = 0;
    }

    private int getParentIndex(int nodeIndex) {
        return (nodeIndex - 1) / 2;
    }

    private int getLeftChildIndex(int nodeIndex) {
        return (2 * nodeIndex) + 1;
    }

    private int getRightChildIndex(int nodeIndex) {
        return (nodeIndex * 2) + 2;
    }

    private boolean isLeaf(int nodeIndex) {
        return getRightChildIndex(nodeIndex) >= size || getLeftChildIndex(nodeIndex) >= size;
    }

    private void swap(int indexOfFirstSwappedElement, int indexOfSecondSwappedElement) {
        Node temp = heap[indexOfFirstSwappedElement];
        heap[indexOfFirstSwappedElement] = heap[indexOfSecondSwappedElement];
        heap[indexOfSecondSwappedElement] = temp;
    }

    public void insert(Node newNode) {
        if (currentIndex < size) {
            heap[currentIndex] = newNode;
            int comparedElementIndex = currentIndex;

            while (getParentIndex(comparedElementIndex) >= 0 && heap[comparedElementIndex].getFrequency() < heap[getParentIndex(comparedElementIndex)].getFrequency()) {
                swap(comparedElementIndex, getParentIndex(comparedElementIndex));
                comparedElementIndex = getParentIndex(comparedElementIndex);
            }
            currentIndex++;
        }
    }

    public Node removeSmallest() {
        Node result = null;
        if (currentIndex > 0) {
            result = heap[0];
            heap[0] = heap[--currentIndex];
            heapify(0);
        }
        return result;
    }

    private void heapify(int startIndex) {
        if (!isLeaf(startIndex)) {
            int leftChildIndex = getLeftChildIndex(startIndex);
            int rightChildIndex = getRightChildIndex(startIndex);

            int freqOfStartIndex = heap[startIndex].getFrequency();
            int freqOfLeftChild = heap[leftChildIndex].getFrequency();
            int freqOfRightChild = heap[rightChildIndex].getFrequency();

            if (freqOfStartIndex > freqOfLeftChild || freqOfStartIndex > freqOfRightChild) {
                if (freqOfLeftChild > freqOfRightChild) {
                    swap(startIndex, rightChildIndex);
                    heapify(rightChildIndex);
                }
                else {
                    swap(startIndex, leftChildIndex);
                    heapify(leftChildIndex);
                }
            }
        }
    }

    public void minimalizeHeap() {
        for (int startingIndex = currentIndex / 2 - 1; startingIndex >= 0; startingIndex--)
            heapify(startingIndex);
    }

    public Node[] getHeap() {
        return heap;
    }
}

class Node {
    private String character;
    private int frequency;
    private Node leftChild;
    private Node rightChild;

    public Node(String character, int frequency){
        this.character = character;
        this.frequency = frequency;
        this.leftChild = null;
        this.rightChild = null;
    }

    public Node(String character, int frequency, Node leftChild, Node rightChild) {
        this.character = character;
        this.frequency = frequency;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return "Node{" +
                "character='" + character + '\'' +
                ", frequency=" + frequency +
                '}';
    }

    public String printTree() {
        String result = this.toString();

        String leftChildResult = "";
        String rightChildResult = "";
        if (this.leftChild != null)
            leftChildResult = this.leftChild.printTree();
        if (this.rightChild != null)
            rightChildResult = this.rightChild.printTree();
        return result + '\'' + leftChildResult + '\'' + rightChildResult;
    }
}


public class Main {

    public static void main(String[] args) {
        int size = 0;
        try {
            String filename = "test.txt";
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                size++;
                myReader.nextLine();
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        MinimalHeap minimalHeap = new MinimalHeap(size);

        try {
            String filename = "test.txt";
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String [] dataSplitted = data.split(" ");
                Node newNode = new Node(dataSplitted[0], Integer.parseInt(dataSplitted[1]));
                minimalHeap.insert(newNode);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        minimalHeap.minimalizeHeap();

        while (minimalHeap.getCurrentIndex() > 1) {
            Node leftChild = minimalHeap.removeSmallest();
            Node rightChild = minimalHeap.removeSmallest();
            Node concatenatedNode = new Node("EMPTY", leftChild.getFrequency() + rightChild.getFrequency(), leftChild, rightChild);
            minimalHeap.insert(concatenatedNode);
        }

        System.out.println(minimalHeap.getHeap()[0].printTree());
    }
}

