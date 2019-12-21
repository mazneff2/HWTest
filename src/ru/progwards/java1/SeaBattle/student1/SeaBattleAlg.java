package ru.progwards.java1.SeaBattle.student1;

import java.util.Arrays;
import java.util.Random;

import ru.progwards.java1.SeaBattle.SeaBattle;
import ru.progwards.java1.SeaBattle.SeaBattle.FireResult;

public class SeaBattleAlg {
    // Тестовое поле создаётся конструктором
    //     SeaBattle seaBattle = new SeaBattle(true);
    //
    // Обычное поле создаётся конструктором по умолчанию:
    //     SeaBattle seaBattle = new SeaBattle();
    //     SeaBattle seaBattle = new SeaBattle(false);
    //
    // Посомтреть результаты стрельбы можно в любой момент,
    // выведя объект класса SeaBattle на консоль. Например так:
    //     System.out.println(seaBattle);
    //
    //
    // Вид тестового поля:
    //
    //           0 1 2 3 4 5 6 7 8 9    координата x
    //         0|.|.|.|.|.|.|.|X|.|.|
    //         1|.|.|.|.|.|X|.|.|.|.|
    //         2|X|X|.|.|.|.|.|.|.|.|
    //         3|.|.|.|.|.|.|.|X|X|X|
    //         4|.|.|.|.|X|.|.|.|.|.|
    //         5|.|.|.|.|X|.|.|Х|.|.|
    //         6|.|.|.|.|.|.|.|Х|.|X|
    //         7|X|.|X|.|.|.|.|Х|.|X|
    //         8|X|.|.|.|.|.|.|X|.|.|
    //         9|X|.|.|.|X|.|.|.|.|.|

	private static final int HORISONTAL = 0b01;
	private static final int VERTIKAL = 0b10;
	char[][] field;
	SeaBattle seaBattle;
	int hits;
	int direction;
	int fireDirection;
	Random random = new Random();
	public static boolean printField = false;
	
	void init(SeaBattle seaBattle) {
	hits = 0;
    	this.seaBattle = seaBattle;
    	field = new char[seaBattle.getSizeX()][];
    	for (int x = 0; x < seaBattle.getSizeX(); x++) {
    		field[x] = new char[seaBattle.getSizeY()];
    		Arrays.fill(field[x], ' ');
    	}
	}
	
	void print() {
		for (int y = 0; y < seaBattle.getSizeY(); y++) {
			String str = "|";
			for (int x = 0; x < seaBattle.getSizeX(); x++) {
				str += field[x][y] + "|";
			}
			System.out.println(str);
		}
		System.out.println("----------------------");
	}
	
	void checkShip(int x, int y) {
		if ((direction&HORISONTAL) != 0) {
			fireDirection = HORISONTAL;
			checkDirection(fire(x-1, y));
			if ((direction&HORISONTAL) != 0)
				checkDirection(fire(x+1, y));
		}
		if ((direction&VERTIKAL) != 0) {
			fireDirection = VERTIKAL;
			if ((direction&VERTIKAL) != 0)
				checkDirection(fire(x, y-1));
			if ((direction&VERTIKAL) != 0)
				checkDirection(fire(x, y+1));
		}
	}
	
	void checkDirection(SeaBattle.FireResult result) {
		switch(result) {
			case DESTROYED :
				direction = 0;
				break;
			case HIT:
				direction = fireDirection;
				break;
			case MISS:;
		}
	}
	
	void markField(int x, int y, SeaBattle.FireResult result) {
		if (result != SeaBattle.FireResult.MISS)
			field[x][y] = 'X';
		else 
			field[x][y] = '*';
	}
	
	void checkHit(SeaBattle.FireResult result) {
		if (printField)
			print();
		if (result != SeaBattle.FireResult.MISS)
			hits++;
	}
	
	void mark(int x, int y) {
		if(x<0 || y<0 || x>=seaBattle.getSizeX() || y>=seaBattle.getSizeY())
			return;
		if (field[x][y] == ' ')
			field[x][y] = '.';
	}
	
	void markHit(int x, int y) {
		mark(x-1, y-1);
		mark(x-1, y);
		mark(x+1, y+1);
		mark(x, y+1);
		mark(x+1, y-1);
		mark(x+1, y);
		mark(x-1, y+1);
		mark(x, y+1);
	}
	
	void markDestroyed() {
    	for (int y = 0; y < seaBattle.getSizeY(); y++) {
        	for (int x = 0; x < seaBattle.getSizeX(); x++) {
        		if (field[x][y] == 'X')
        			markHit(x, y);
        	}
    	}
	}
	
	SeaBattle.FireResult fire(int x, int y) {
		if(x<0 || y<0 || x>=seaBattle.getSizeX() || y>=seaBattle.getSizeY() || hits >= 20)
			return SeaBattle.FireResult.MISS;
		if (field[x][y] == ' ') {
			SeaBattle.FireResult result = seaBattle.fire(x, y);
			markField(x, y, result);
			checkHit(result);
			if (result == SeaBattle.FireResult.HIT) {
				direction = fireDirection;
				checkShip(x, y);
			}
			else
				if (result == SeaBattle.FireResult.DESTROYED)
					markDestroyed();
			
			return result;
		}
		return SeaBattle.FireResult.MISS;
	}

	int getRandom() {
		double d = random.nextDouble();
		return (int)Math.floor(d*10);
	}
	
    // пример алгоритма:
    // стрельба по всем квадратам поля полным перебором
	void algorithm1() {
    	for (int y = 0; y < seaBattle.getSizeY(); y++) {
        	for (int x = 0; x < seaBattle.getSizeX(); x++) {
        		fireDirection = VERTIKAL | HORISONTAL;
        		fire(x, y);
            }
        }
	}

	void algorithm2() {
		while(hits < 20) {
    		fireDirection = VERTIKAL | HORISONTAL;
			fire(getRandom(), getRandom());
		}
	}
	
	void stepFire(int offset) {
    	for (int y = 0; y < seaBattle.getSizeY(); y++) {
        	for (int x = y+offset; x < seaBattle.getSizeX(); x+=4) {
        		fireDirection = VERTIKAL | HORISONTAL;
        		fire(x, y);
            }
        	for (int x = y-offset; x >= 0; x-=4) {
        		fireDirection = VERTIKAL | HORISONTAL;
        		fire(x, y);
            }
    	}
	}
	
	void algorithm3() {
        	stepFire(0);
        	stepFire(2);
        	stepFire(1);
        	stepFire(3);
        }
	
    public void battleAlgorithm(SeaBattle seaBattle) {
    	init(seaBattle);
    	algorithm3();
    }

    static void fullTest() {
    	SeaBattleAlg.printField = false;
    	double result = 0;
    	for(int i=0; i<1000; i++) {
    		SeaBattle seaBattle = new SeaBattle();
    		new SeaBattleAlg().battleAlgorithm(seaBattle);
    		result += seaBattle.getResult();
    	}
    	System.out.println(result/1000);
    }
    
    static void oneTest() {
    	SeaBattleAlg.printField = true;
		SeaBattle seaBattle = new SeaBattle(true);
		new SeaBattleAlg().battleAlgorithm(seaBattle);
		System.out.println(seaBattle.getResult());
	}
    
    // функция для отладки
    public static void main(String[] args) {
    	System.out.println("Sea battle");
    	fullTest();
    }
}

