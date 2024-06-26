package businessObject;

import java.util.List;

import database.QuizDao;
import entity.Game;
import entity.GameMode;
import entity.Quiz;

// クイズを取得するクラス
public class SetGameLogic {
	public static Game execute(GameMode mode) {
		//ゲームインスタンス生成
		Game game = new Game(mode);

		//ランダムな10個のidが並んだリストを作る
		//このロジックはデータベースのORDER BY句で「Random()」と並び順を指定すれば必要ないことに気づいた
		//宣言
		int count = mode.getQuizNum(); //問題数分格納するためのカウント

		//クイズテーブルの全Idを取得
		QuizDao dao = new QuizDao();
		List<String> allQuizIds = dao.selectAllQuizId(mode);

		while (count != 0) {
			//最大の要素数までの数字をランダムに取得する
			int randomIterator = new java.util.Random().nextInt(allQuizIds.size());
			System.out.println("イテレータ：" + randomIterator);
			//id取得し、ランダムなIDを引数にクイズを取得する
			String randomId = allQuizIds.get(randomIterator);
			System.out.println("randomID:" + randomId);
			Quiz quiz = dao.selectQuizById(mode, randomId);

			//取得したクイズを格納する。
			game.getQuizzes().put(count, quiz);

			//クイズの結果は一旦falseで置く
			game.getIsCorrects().put(count, false);

			//クイズを格納したのでカウントを減らす。
			count--;

			//データベースに10問以上あれば、問題の重複を排除するために
			//全IDリストから削除する
			if (allQuizIds.size() >= mode.getQuizNum()) {
				allQuizIds.remove(randomIterator);
			}
		}
		//これで問題数分のクイズリストが格納された状態になる。

		return game;
	}
}
