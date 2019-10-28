package uon.inft2051.lab04;

import com.codename1.ui.TextSelection;

public class Turn {
    public boolean attackUsed;
    Character Char;
    MoveCircle circle;
    boolean turn = true;
    boolean enemyTurn = false;

    private boolean isTurn;
    public Turn (Character Char, MoveCircle circle)
    {
        this.Char = Char;
        this.circle = circle;
    }
    public boolean isTurn()
    {
        return turn;
    }
    public boolean isEnemyTurn()
    {
        return enemyTurn;
    }
    public void enemyMove()
    {
        enemyTurn = true;
    }

    public void startTurn()
    {
        turn = true;
        attackUsed = false;
        circle.setCenter(Char);
    }

    public void attack () {
        attackUsed = true;
    }

    public boolean getAttackUsed () {
        return attackUsed;
    }

    public void endTurn()
    {
        turn = false;
        enemyTurn = false;
    }
}
