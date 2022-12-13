import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;

public class othello extends Frame implements KeyListener,MouseListener{
  private static int mode = 0;
  int MASU = 8;//盤のマス目
  int GS = 50;//マス目の大きさ
  int WIDTH = 400;//キャンバスの幅
  int HEIGHT = 420;//キャンバスの高さ
  int[] [] board = new int[MASU][MASU];//マスを配列で管理
  boolean jyunban = false;//順番をbooleanで示すtrue = 白、false = 黒
  int a = 0;//countblackで使う変数
  int b = 0;//countwhiteで使う変数
  String c;//winnerで使う変数
  Timer timer1;//Timertask1で使う
  Timer timer2;//Timertask2で使う

  Othello22(){
    this.setSize(WIDTH,HEIGHT+60);//ゲーム画面の大きさ
    this.setVisible(true);//可視化
    board[3][3] = board[4][4] = 2;//2を白の駒とする
    board[3][4] = board[4][3] = 1;//1を黒の駒とする
    this.addKeyListener(this);//キーイベント
    this.addMouseListener(this);//マウスイベント
  }
  public void update(Graphics gc){
    paint(gc);//paint()実行
  }

  public void paint(Graphics g){
    switch(mode){
    case 0://スタート画面
      g.setColor(Color.black);
      g.setFont(new Font("Arial",Font.PLAIN,20));
      g.drawString("           start Othello",WIDTH/2-120,(HEIGHT+20)/2+20);//スペースキー押したらcase 1へ
      break;
    case 1://ゲーム画面
      drawboard(g);//関数呼び出し
      timer1 = new Timer();
      timer1.schedule(new Timertask1(),0,1000);//Timertask1呼び出し
      timer2 = new Timer();
      timer2.schedule(new Timertask2(),0,1000);//Timertask2呼び出し
      break;
    case 2://ゲーム終了時
      g.setColor(Color.black);
      timer1.cancel();//黒タイマー停止
      timer2.cancel();//白タイマー停止
      /*countblack(),countwhite()で駒数数えてwinner()で勝利判定*/
      g.drawString(                     countblack(a)+"対"+countwhite(b)+"で勝者は"+winner(c),WIDTH/2-100,HEIGHT+45);
      break;
    }
  }
/*オセロ盤面表示*/
  public void drawboard(Graphics g){
    g.setColor(new Color(0,128,128));//盤面色
    g.fillRect(0,50,WIDTH,HEIGHT-20);//盤面の大きさ
    for(int i = 1; i < MASU; i++) {//縦線を引く
        g.setColor(Color.black);
        g.drawLine(i*GS, 50, i*GS,HEIGHT+30);
      }
      for(int i = 1; i < MASU; i++) {//横線を引く
        g.setColor(Color.black);
        g.drawLine(0, i*GS+50, WIDTH, i*GS+50);
      }
      g.drawRect(0, 50,WIDTH,HEIGHT-20);//外枠を引く
      draw(g);//関数呼び出し
  }
/*駒を描写*/
  public void draw(Graphics g){
    for(int mx = 0; mx < MASU; mx ++){//x軸
      for(int my = 0; my < MASU; my++){//y軸
        if(board[mx][my]== 1){//配列が1なら黒駒描写
          g.setColor(Color.black);
          g.fillOval(GS*mx+5, GS*my+55, GS*8/10, GS*8/10);//駒の大きさ
        }else if(board[mx][my] == 2){//配列が2なら白駒描写
          g.setColor(Color.white);
          g.fillOval(GS*mx+5, GS*my+55, GS*8/10, GS*8/10);//駒の大きさ
        }
      }
    }
  }

/*クリックされた時の処理*/
  public void Clicked(int mx,int my){//MouseEventのmx,myが引数
    int stone;//駒を描写するため、順番をint型で表示
      if(jyunban == true){//順番が白なら
        stone = 2;
        }
      else{//順番が黒なら
        stone = 1;
      }
    board[mx][my] = stone;//順番に応じて、代入
    reverse(mx,my);//関数呼び出し
    jyunban = !jyunban;//最後に順番交代
  }
/*置けるかの判定*/
  public boolean judge(int mx,int my){//MouseEventのmx,myが引数
    if (mx >= MASU || my >= MASU){//mx,myがマス外だったら
      return false;//false返す
    }
    if (board[mx][my] == 1 || board[mx][my] == 2){//すでに駒が置かれていたとしたら
      return false;//false返す
    }
    /*関数呼び出し*/
    if (judge1(mx, my, 1, 0)){//右隣についての判断がtrueなら
      return true;//true返す
    }else if (judge1(mx, my, 0, 1)){//下についての判断がtrueなら
      return true;//true返す
    }else if (judge1(mx, my, -1, 0)){//左隣についての判断がtrueなら
      return true;//true返す
    }else if (judge1(mx, my, 0, -1)){//上についての判断がtrueなら
      return true;//true返す
    }else if (judge1(mx, my, 1, 1)){//右下についての判断がtrueなら
      return true;//true返す　
    }else if (judge1(mx, my, -1, -1)){//左上についての判断がtrueなら
      return true;//true返す
    }else  if (judge1(mx, my, 1, -1)){//右上についての判断がtrueなら
      return true;//true返す
    }else if (judge1(mx, my, -1, 1)){//左下についての判断がtrueなら
      return true;//true返す
    }else {//8方向の判断ですべてfalseが返されたら
      return false;//falseを返す
    }
  }
/*8方向についての判定*/
  public boolean judge1(int mx ,int my , int dx ,int dy){//dx,dyは方向
    int Stone;
      if(jyunban == true){
        Stone = 2;
        }
      else{
        Stone = 1;
      }
    /*その方向の一つ隣をみていく*/
    mx += dx;
    my += dy;
    if(mx < 0 || mx >= MASU || my < 0 || my >= MASU)//盤面外の時
      return false;//falseを返す
    if(board[mx][my] == Stone)//同色の駒だったら
      return false;//falseを返す
    if(board[mx][my] != 1 && board[mx][my] != 2)//駒が何も置かれていなかったら
      return false;//falseを返す
    /*もう一つ隣を調べる*/
    mx += dx;
    my += dy;
    while (mx >= 0 && mx < MASU && my >= 0 && my < MASU) {//盤面外にならない限り
      if (board[mx][my] != 1 && board[mx][my] != 2)//駒が何も置かれていなかったら
          return false;//falseを返す
      if (board[mx][my] == Stone)//同色の駒が見つかったら
          return true;//trueを返す
      /*さらに隣を調べていく*/
      mx += dx;
      my += dy;
    }
    return false;//falseを返す
  }
/*反転の関数*/
  public void reverse(int mx,int my){//MouseEventのmx,myが引数
    if(judge1(mx,my,1,0)) reverse(mx,my,1,0);//judge右がtrueなら関数呼び出し
    if(judge1(mx,my,1,1)) reverse(mx,my,1,1);//judge右下がtrueなら関数呼び出し
    if(judge1(mx,my,0,1))  reverse(mx,my,0,1);//judge下がtrueなら関数呼び出し
    if(judge1(mx,my,-1,-1)) reverse(mx,my,-1,-1);//judge左上がtrueなら関数呼び出し
    if(judge1(mx,my,-1,0)) reverse(mx,my,-1,0);//judge左がtrueなら関数呼び出し
    if(judge1(mx,my,-1,1)) reverse(mx,my,-1,1);//judge左下がtrueなら関数呼び出し
    if(judge1(mx,my,0,-1)) reverse(mx,my,0,-1);//judge上がtrueなら関数呼び出し
    if(judge1(mx,my,1,-1)) reverse(mx,my,1,-1);//judge右上がtrueなら関数呼び出し
  }
/*8方向の反転*/
  public void reverse(int mx,int my,int dx,int dy){
    int SStone;//駒を描写（反転）するためにint型宣言
    if(jyunban == true){//白なら
      SStone = 2;
      System.out.println("次は黒の番です");
    }else {//黒なら
      SStone = 1;
      System.out.println("次は白の番です");
    }
    /*一つ隣を調べる*/
    mx += dx;
    my += dy;
    while (board[mx][my] !=SStone) {//同色の駒が見つかるまで
      board[mx][my] = SStone;//打つ色と同じ
      repaint();//更新されていくので再描写
      /*さらに隣を調べる*/
      mx += dx;
      my += dy;
    }
  }

/*黒の駒カウント*/
  public int countblack(int black){
    for(int i=0; i<MASU; i++){
      for(int n=0; n<MASU; n++){
        if(board[i][n] == 1){
          black += 1;//見つけたら1足していく
        }
      }
    }
    return black;//数を返す
  }
/*白の駒カウント*/
  public int countwhite(int white){
    for(int i=0; i<MASU; i++){
      for(int n=0; n<MASU; n++){
        if(board[i][n] == 2){
          white += 1;//見つけたら1足していく
        }
      }
    }
    return white;//数を返す
  }

/*勝者判定*/
  public String winner(String winner){
    if(countblack(a) > countwhite(b))//黒駒 > 白駒
      winner = "黒!!";//String型のwinnerに代入
    if(countblack(a) < countwhite(b))//黒駒 < 白駒
      winner = "白!!";//String型のwinnerに代入
    if(countblack(a) == countwhite(b))//黒駒 = 白駒
      winner = "いません";//String型のwinnerに代入
    return winner;//winnerを返す
  }
/*終了かどうかの判定*/
  public boolean finish(){
    int k = 0;//何手打ったか
    int Black = 0;//黒駒の数
    int White = 0;//白駒の数
    for(int i = 0; i < MASU; i++){
      for(int n = 0; n < MASU; n++){
        if(board[i][n] == 1 || board[i][n] == 2)//どちらかの駒が置かれているなら
        {k += 1;}//kに１を足していく
        if(board[i][n] == 1)//黒駒を見つけたら
        {Black += 1;} //足していく
        if(board[i][n] == 2)//白駒を見つけたら
        {White += 1;}//足していく
      }
    }
    if(k == 64){//64の駒が見つかったら（盤面すべて埋まったら)
      return true;//true返す
    }else if(k == Black || k == White){//黒駒or白駒がkと等しいなら盤面が黒駒か白駒なはず
      return true;//true返す
    }else{
      return false;//false返す
    }
  }

  public static void main(String [] args) {
    new Othello22();
  }
  /*キーイベント*/
  public void keyTyped(KeyEvent ke){}
  public void keyReleased(KeyEvent ke){}
  public void keyPressed(KeyEvent ke){
    int cd = ke.getKeyCode();//キー取得
    if(cd == KeyEvent.VK_SPACE){//スペースキーが押されたら
      if(this.mode == 0){//スタート画面なら
        System.out.println("start");
        this.mode++;//ゲーム画面へ
        repaint();//再描写
      }else if(this.mode == 1){
        this.mode = 2;
        repaint();
      }
    }
    if(cd == KeyEvent.VK_ENTER){//エンターキーが押されたら
      if(this.mode == 1){//ゲーム画面なら
        if(finish() == false){//終了判定がfalseならパス
          System.out.println("パスしました");
          jyunban =! jyunban;//順番交代
        }
        else{//trueなら
          this.mode = 2;//終了画面へ
          repaint();//再描写
        }
      }
    }
  }
/*マウスイベント*/
  public void mouseClicked(MouseEvent e){
    if(this.mode == 1){//ゲーム画面なら
    /*マウスで取得した座標÷マスの大きさでどこのマス目かを計算*/
      int mx = e.getX()/GS;//x座標
      int my = (e.getY()-30)/GS;//y座標
      if(judge(mx,my)){//judgeがtrueなら
        Clicked(mx,my);//関数呼び出し
      }else{//falseなら
        System.out.println("ここには置けません");
      }
    }
  }
  public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}

  class Timertask1 extends TimerTask{//黒のタイマー
    int s = 11;//10秒カウントにしたいので11と設定
    public void run(){
      if(jyunban == false){//黒の順番の時だけカウント
        s -= 1;//引いていく
        System.out.println("黒:"+s);
      }else if(jyunban == true){//白の時
        this.cancel();//カウント終了
      }
      if(s == 0){//黒が時間切れになった時の処理
        this.cancel();//これがないとjyunbanが二回変わって、黒の番になってしまう
        jyunban =!jyunban;
        System.out.println("次は白の番です");
      }
    }
  }

  class Timertask2 extends TimerTask{//白のタイマー
    int m = 11;//10秒カウントにしたいので11と設定
    public void run(){
      if(jyunban == true){//白の時、カウント開始
        m -= 1;//引いていく
        System.out.println("白:"+m);
      }else if(jyunban == false){//黒の時
        this.cancel();//カウント終了
      }
      if(m == 0){//黒が時間切れになった時の処理
        this.cancel();//これがないとjyunbanが二回変わって、白の番になってしまう
        jyunban =!jyunban;
        System.out.println("次は黒の番です");
      }
    }
  }
}