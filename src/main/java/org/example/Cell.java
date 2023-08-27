package org.example;

//the class of Cell in Queue , that is a member of the SharedSwingWorker
class Cell{
    private boolean select;
    private int row;
    private String todo;

    public Cell(boolean select, int row, String todo) {
        this.select = select;
        this.row = row;
        this.todo = todo;
    }

    public boolean isSelect() {
        return select;
    }

    public int getRow() {
        return row;
    }

    public String getTodo() {
        return todo;
    }
}


