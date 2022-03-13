package com.example.gamesos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

public class Online implements Initializable {
    private Parent root;
    public static OCo[][] oco = new OCo[7][7];
    static int dp1=0,dp2=0,x,y,luot,codiem=0,sl=0;
    static boolean denluot1,denluot2;
    int tt=0,rs=0,ex=0;
    static MediaPlayer mediabg,mediaEv;
    static Map<String, Button> map = new HashMap<String, Button>();
    private static final String CLOUDAMQP_URL = "amqps://wqfqgvss:c2uqBdDCrPD1cV7Dlum-BAssu_yV15Q5@kangaroo.rmq.cloudamqp.com/wqfqgvss";
    private static final String EXCHANGE_NAME = "QUEUE";
    com.rabbitmq.client.Connection connection1;
    String queueName;
    Channel channel;
    String Sclick = "sound/click.mp3";
    String Sbackground = "sound/play.mp3";
    String Swin = "sound/win.mp3";
    String Sscore= "sound/score.mp3";

    @FXML Button o00,o01,o02,o03,o04,o05,o06,o10,o11,o12,o13,o14,o15,o16,o20,o21,o22,o23,o24,o25,o26;
    @FXML Button o30,o31,o32,o33,o34,o35,o36,o40,o41,o42,o43,o44,o45,o46,o50,o51,o52,o53,o54,o55,o56;
    @FXML Button o60,o61,o62,o63,o64,o65,o66,btn_rf;
    @FXML
    private ImageView lgp1;
    @FXML
    private ImageView lgp2;
    @FXML
    private AnchorPane banco,chon;
    @FXML
    private Label diem1;
    @FXML
    private Label diem2;
    @FXML
    private AnchorPane p2win;
    @FXML
    private Label diem22;
    @FXML
    private AnchorPane p1win;
    @FXML
    private Label diem11;
    @FXML
    private AnchorPane hoa;
    @FXML
    private Label diem111;
    //===================================================================================
    public void online() throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(CLOUDAMQP_URL);
        System.out.println("Create a Connection");
        factory.setRequestedHeartbeat(30);
        factory.setConnectionTimeout(30000);
        connection1 = factory.newConnection();
        channel = connection1.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("Create a queue " + EXCHANGE_NAME);
        channel.queueDeclare(EXCHANGE_NAME, false, false, false, null);
        System.out.println("Start  ... ");
    }


    public void guico(String msg) throws IOException {
        channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + msg + "'");
    }
    public void nhanco() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            String[] str = message.split(" ");
            int i = Integer.parseInt(str[0]);
            int j = Integer.parseInt(str[1]);
            String value = str[2];
            if(value.equals("ex"))
            {
                ex+=1;
                if(ex>=1)
                {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Đối thủ đã thoát, exit!");
                            alert.showAndWait();
                            banco.setDisable(true);
                            btn_rf.setDisable(true);
                        }
                    });
                }
            }
            if(value.equals("rs"))
            {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        chonrf.setVisible(true);
                    }
                });
            }
            if(value.equals("crs"))
            {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(i==1)
                            rs+=1;
                        if(i==0)
                        {
                            rs=0;
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Có người không đồng ý tạo mới!");
                            alert.showAndWait();
                            chonrf.setVisible(false);
                        }
                        if(rs==2)
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Đối phương đã đồng ý tạo mới!");
                            alert.showAndWait();
                            rsbanco();
                            rs=0;
                        }
                    }
                });
            }
            if(value.equals("st"))
            {
                if(tt<2)
                    tt+=1;
                if(tt==2)
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            banco.setDisable(false);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Kết nối thành công!");
                            alert.showAndWait();
                        }
                });

            }
            if(value.equals("O")||value.equals("S"))
            {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        show_luot();
                        if(oco[i][j].getTT()==0)
                            add1(i,j,value);
                    }
                });
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
    public void taomap()
    {
        map = new HashMap<String, Button>();
        map.put("o00",o00);map.put("o01",o01);map.put("o02",o02);map.put("o03",o03);map.put("o04",o04);map.put("o05",o05);map.put("o06",o06);
        map.put("o10",o10);map.put("o11",o11);map.put("o12",o12);map.put("o13",o13);map.put("o14",o14);map.put("o15",o15);map.put("o16",o16);
        map.put("o20",o20);map.put("o21",o21);map.put("o22",o22);map.put("o23",o23);map.put("o24",o24);map.put("o25",o25);map.put("o26",o26);
        map.put("o30",o30);map.put("o31",o31);map.put("o32",o32);map.put("o33",o33);map.put("o34",o34);map.put("o35",o35);map.put("o36",o36);
        map.put("o40",o40);map.put("o41",o41);map.put("o42",o42);map.put("o43",o43);map.put("o44",o44);map.put("o45",o45);map.put("o46",o46);
        map.put("o50",o50);map.put("o51",o51);map.put("o52",o52);map.put("o53",o53);map.put("o54",o54);map.put("o55",o55);map.put("o56",o56);
        map.put("o60",o60);map.put("o61",o61);map.put("o62",o62);map.put("o63",o63);map.put("o64",o64);map.put("o65",o65);map.put("o66",o66);
    }
    public void khoitao()
    {
        dp1=0;
        dp2=0;
        sl=0;
        luot =1;
        denluot1=true;
        denluot2=true;
        tt=0;rs=0;ex=0;
        int i,j;
        for(i=0;i<7;i++)
            for(j=0;j<7;j++)
            {
                oco[i][j] = new OCo("sos",0);
                String id="o"+i+j;
                Button btn = map.get(id);
                btn.setText("");
                btn.setDisable(false);
                tomau(i,j,0);
            }
        show_luot();
        diem1.setText(String.valueOf(dp1));
        diem2.setText(String.valueOf(dp2));
        volOff.setVisible(false);
        volOn.setVisible(true);
        p1win.setVisible(false);
        p2win.setVisible(false);
        hoa.setVisible(false);
        System.out.println("Khoi tao thanh cong!");
        music(Sbackground,0.15);
    }
    //Các điều kiện-----------------------------------------------------------------------
    public void kt_huongS(int h,int i,int j)
    {
        switch (h)
        {
            case 1: if(oco[i-1][j-1].getGT().equals("O")&&oco[i-2][j-2].getGT().equals("S"))
                congdiem(i,j,i-1,j-1,i-2,j-2); break;
            case 2: if(oco[i-1][j].getGT().equals("O")&&oco[i-2][j].getGT().equals("S"))
                congdiem(i,j,i-1,j,i-2,j); break;
            case 3:if(oco[i-1][j+1].getGT().equals("O")&&oco[i-2][j+2].getGT().equals("S"))
                congdiem(i,j,i-1,j+1,i-2,j+2); break;
            case 4: if(oco[i][j+1].getGT().equals("O")&&oco[i][j+2].getGT().equals("S"))
                congdiem(i,j,i,j+1,i,j+2); break;
            case 5: if(oco[i+1][j+1].getGT().equals("O")&&oco[i+2][j+2].getGT().equals("S"))
                congdiem(i,j,i+1,j+1,i+2,j+2); break;
            case 6: if(oco[i+1][j].getGT().equals("O")&&oco[i+2][j].getGT().equals("S"))
                congdiem(i,j,i+1,j,i+2,j); break;
            case 7:if(oco[i+1][j-1].getGT().equals("O")&&oco[i+2][j-2].getGT().equals("S"))
                congdiem(i,j,i+1,j-1,i+2,j-2); break;
            case 8: if(oco[i][j-1].getGT().equals("O")&&oco[i][j-2].getGT().equals("S"))
                congdiem(i,j,i,j-1,i,j-2); break;
        }
    }
    public void kt_huongO(int h,int i,int j)
    {
        switch (h)
        {
            case 1: if(oco[i-1][j-1].getGT().equals("S")&&oco[i+1][j+1].getGT().equals("S"))
                congdiem(i,j,i-1,j-1,i+1,j+1);
                break;
            case 2: if(oco[i-1][j].getGT().equals("S")&&oco[i+1][j].getGT().equals("S"))
                congdiem(i,j,i-1,j,i+1,j);
                break;
            case 3:if(oco[i-1][j+1].getGT().equals("S")&&oco[i+1][j-1].getGT().equals("S"))
                congdiem(i,j,i-1,j+1,i+1,j-1);
                break;
            case 4: if(oco[i][j-1].getGT().equals("S")&&oco[i][j+1].getGT().equals("S"))
                congdiem(i,j,i,j-1,i,j+1);
                break;
        }
    }
    public void tinhdiem(int i, int j,String x)
    {
        if(x.equals("S"))
        {
            if(i<2)
            {
                if (j < 2)
                {
                    kt_huongS(4, i, j);
                    kt_huongS(5, i, j);
                    kt_huongS(6, i, j);
                }
                if(j>1&&j<5)
                {
                    kt_huongS(4, i, j);
                    kt_huongS(5, i, j);
                    kt_huongS(6, i, j);
                    kt_huongS(7, i, j);
                    kt_huongS(8, i, j);
                }
                if (j > 4)
                {
                    kt_huongS(6, i, j);
                    kt_huongS(7, i, j);
                    kt_huongS(8, i, j);
                }
            }
            if(i<5&&i>1)
            {
                if (j < 2)
                {
                    kt_huongS(2, i, j);
                    kt_huongS(3, i, j);
                    kt_huongS(4, i, j);
                    kt_huongS(5, i, j);
                    kt_huongS(6, i, j);
                }
                if(j>1&&j<5)
                {
                    kt_huongS(1, i, j);
                    kt_huongS(2, i, j);
                    kt_huongS(3, i, j);
                    kt_huongS(4, i, j);
                    kt_huongS(5, i, j);
                    kt_huongS(6, i, j);
                    kt_huongS(7, i, j);
                    kt_huongS(8, i, j);
                }
                if (j > 4)
                {
                    kt_huongS(1, i, j);
                    kt_huongS(2, i, j);
                    kt_huongS(6, i, j);
                    kt_huongS(7, i, j);
                    kt_huongS(8, i, j);
                }
            }
            if(i>4)
            {
                if (j < 2)
                {
                    kt_huongS(2, i, j);
                    kt_huongS(3, i, j);
                    kt_huongS(4, i, j);
                }
                if(j>1&&j<5)
                {
                    kt_huongS(4, i, j);
                    kt_huongS(3, i, j);
                    kt_huongS(2, i, j);
                    kt_huongS(1, i, j);
                    kt_huongS(8, i, j);
                }
                if (j > 4)
                {
                    kt_huongS(2, i, j);
                    kt_huongS(1, i, j);
                    kt_huongS(8, i, j);
                }
            }
        }
        if(x.equals("O"))
        {
            if(i==0 || i==6)
                if (j > 0 && j < 6)
                    kt_huongO(4, i, j);
            if(j==0 || j==6)
                if (i > 0 && i < 6)
                    kt_huongO(2, i, j);
            if(i>0 && i<6 && j>0 && j<6)
            {
                kt_huongO(1,i,j);
                kt_huongO(2,i,j);
                kt_huongO(3,i,j);
                kt_huongO(4,i,j);
            }
        }
    }

    int kt_thang()
    {
        if(dp1==dp2)
            return 0;
        else
        {
            if(dp1>dp2) return 1;
            else return 2;
        }
    }
    //Các sự kiện-----------------------------------------------------------------------
    void show_luot()
    {
        Thread thread1 =new Thread(new Runnable() {
            @Override
            public void run() {
                if(luot==1)
                {
                    lgp1.setVisible(true);
                    lgp2.setVisible(false);
                }
                if(luot==2)
                {
                    lgp1.setVisible(false);
                    lgp2.setVisible(true);
                }
            }
        });
        thread1.start();
    }
    public void congdiem(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        soundEv(Sscore,2);
        if(luot==1)
        {
            dp1+=1;
            codiem=1;
            System.out.println("P1 +1");
            tomau(x1,y1,1);
            tomau(x2,y2,1);
            tomau(x3,y3,1);
        }
        else
        {
            dp2+=1;
            codiem=2;
            System.out.println("P2 +1");
            tomau(x1,y1,2);
            tomau(x2,y2,2);
            tomau(x3,y3,2);
        }
    }
    void doiluot()
    {
        if(luot==1)
            luot = 2;
        else
            luot=1;
    }
    void tomau(int x,int y,int tt)
    {
        int mau=tt;
        String id ="o"+x+y;
        if((tt==1 && oco[x][y].getTT()==2) || (tt==2 && oco[x][y].getTT()==1))
            mau=3;
        if(oco[x][y].getTT()==3)
            mau=0;
        if(mau==1)
        {
            map.get(id).setStyle("-fx-background-color: #b80000;");
            oco[x][y].setTT(1);
        }

        if(mau==2)
        {
            map.get(id).setStyle("-fx-background-color: #2187ed;");
            oco[x][y].setTT(2);
        }
        if(mau==3)
        {
            map.get(id).setStyle("-fx-background-color: #ffa200;");
            oco[x][y].setTT(3);
        }
        if(mau==0)
        {
            map.get(id).setStyle("-fx-background-color: #f0fafa;");
            oco[x][y].setTT(0);
        }
    }

    public void music(String str, double vol){
        Thread thread1 =new Thread(new Runnable() {
            @Override
            public void run() {
                if(mediabg != null) {
                    mediabg.stop();
                }
                Media hit1 = new Media(new File(str).toURI().toString());
                mediabg = new MediaPlayer(hit1);
                mediabg.setVolume(vol);
                mediabg.play();
            }
        });
        thread1.start();
    }
    public void soundEv(String str, double vol){
        Thread thread1 =new Thread(new Runnable() {
            @Override
            public void run() {
                if(mediaEv != null) {
                    mediaEv.stop();
                }
                Media hit2 = new Media(new File(str).toURI().toString());
                mediaEv = new MediaPlayer(hit2);
                mediaEv.setVolume(vol);
                mediaEv.play();
            }
        });
        thread1.start();
    }
    //Screen-----------------------------------------------------------------------
    @FXML
    void rsbanco() {
        khoitao();
    }
    @FXML
    void toMenu(ActionEvent event) throws IOException, TimeoutException {
        ex=-10;
        String msg =0+" "+0+" "+"ex";
        guico(msg);
        root = FXMLLoader.load(getClass().getResource("view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        mediabg.stop();
        channel.close();

    }
    //----------------Danhco---------------------------------
    @FXML Button btn_st;
    @FXML
    void start(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        btn_st.setVisible(false);
        String msg =0+" "+0+" "+"st";
        guico(msg);
        if(tt==0){
        alert.setHeaderText("Chờ kết nối!");
        alert.showAndWait();}
        //banco.setDisable(false);
    }
    //====================Đánh 1 nước cờ====================//
    @FXML
    void DanhCo(ActionEvent event) {
        show_luot();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if((denluot1&&luot==1) || (denluot2&&luot==2))
        {
            if(sl==1)
            {
                denluot1=false;
                alert.setHeaderText("Bạn là người chơi 2");
                alert.showAndWait();
            }

            if(sl==0)
            {
                denluot2=false;
                alert.setHeaderText("Bạn là người chơi 1");
                alert.showAndWait();
            }
            Button btn = (Button) event.getSource();
            String id = btn.getId();
            x= Integer.parseInt(id.substring(1,2));
            y= Integer.parseInt(id.substring(2));
            oco[x][y]=new OCo();
            chon.setVisible(true);
            banco.setDisable(true);

        }
        else
        {
            alert.setHeaderText("Chưa đến lượt của bạn!");
            alert.showAndWait();
        }

    }
    @FXML
    void add(ActionEvent event) throws IOException {

        String value = ((Button)event.getSource()).getText();
        String msg = x+" "+y+" "+value;
        guico(msg);
        soundEv(Sclick,2);
        chon.setVisible(false);
        banco.setDisable(false);

    }
    @FXML
    void add1(int x1, int y1,String value ) throws IllegalStateException
    {
        String id ="o"+x1+y1;
        System.out.println(id+": "+value);
        oco[x1][y1]=new OCo(value,4);
        Button btn = map.get(id);
        btn.setText(value);
        btn.setDisable(true);
        tinhdiem(x1,y1,value);
        if(codiem==0){
            doiluot();
        }
        else {
            if (codiem == 1) {
                luot=1;
            } else {
                luot=2;
            }
            codiem=0;
        }
        System.out.println("Luot : P"+luot);
        show_luot();
        diem1.setText(String.valueOf(dp1));
        diem2.setText(String.valueOf(dp2));
        sl+=1;
        if(sl==49)
        {
            if(kt_thang()==1)
            {
                diem11.setText(String.valueOf(dp1));;
                music(Swin,1);
                p1win.setVisible(true);
                banco.setDisable(false);
            }
            if(kt_thang()==2)
            {
                diem22.setText(String.valueOf(dp2));
                music(Swin,1);
                p2win.setVisible(true);
                banco.setDisable(false);
            }
            if(kt_thang()==0)
            {
                diem111.setText(String.valueOf(dp1));
                music(Swin,1);
                hoa.setVisible(true);
                banco.setDisable(false);
            }
        }
    }
    //====================Bỏ chọn để đánh ô khác====================//
    @FXML
    void tatchon(ActionEvent event) {
        chon.setVisible(false);
        banco.setDisable(false);
    }
    //====================Yeu cau reset====================//
    @FXML
    AnchorPane chonrf;
    @FXML
    void chonrs(ActionEvent event) throws IOException {
        String id = ((Button)event.getSource()).getId();
        if(id.equals("ys"))
        {
            if(rs==0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Chờ phản hồi từ đối thủ!");
                alert.showAndWait();
            }
            String msg =1+" "+0+" crs";
            guico(msg);
        }
        if(id.equals("no"))
        {
            String msg =0+" "+0+" crs";
            guico(msg);
        }
        chonrf.setVisible(false);
    }
    @FXML
    void refresh(ActionEvent event) throws IOException {
        String msg =0+" "+0+" rs";
        guico(msg);
    }

    //====================Tat/Bat nhac nen====================//
    @FXML
    private Button volOff;
    @FXML
    private Button volOn;
    @FXML
    void stopS(ActionEvent event) {
        if(mediabg!=null){
            mediabg.pause();
            volOn.setVisible(false);
            volOff.setVisible(true);}
    }
    @FXML
    void playS(ActionEvent event) {
        if(mediabg!=null){
            Duration i1 = mediabg.getCurrentTime();
            Duration i2 = mediabg.getStopTime();
            if(i1.equals(i2))
                music(Sbackground,0.15);
        mediabg.play();}
        volOff.setVisible(false);
        volOn.setVisible(true);
    }
    //================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taomap();
        khoitao();
        show_luot();
        try {
            online();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        try {
            nhanco();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
