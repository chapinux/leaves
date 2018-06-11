import leaves.Model

/**
  * Created by julien on 16/06/17.
  */
object ModelApp extends App {

  override def main(args: Array[String]) = {
    //    val alphaShape=60.0115243747524
    //    val angle0=12.629477195055273
    //    val angle1=92.8763826709794
    //    val angle2=163.07406501791252
    //    val angle3=69.57126530044215
    //    val angle4=29.01829363707811
    //    val decreaseRate0=1.1132531713464238
    //    val decreaseRate1=1.2865085427068383
    //    val decreaseRate2=1.4398920161816144
    //    val decreaseRate3=1.3960773263769433
    //    val decreaseRate4=0.8639791997847552
    //    val nbBifurcation0=5.0
    //    val nbBifurcation1=5.23674897022271
    //    val nbBifurcation2=2.426857540631492
    //    val nbBifurcation3=2.7907624089997944
    //    val nbBifurcation4=2.998488106014972
    //    val seed=450148026477671541L
    //    val sterilityRate0=0.4532968256081378
    //    val sterilityRate1=0.33389044073912344
    //    val sterilityRate2=0.7066806706632287
    //    val sterilityRate3=0.20494417286417366
    //    val sterilityRate4=0.9447860564321984
    //    val thickness0=2.476429203518445
    //    val thickness1=3.6141444211266105
    //    val thickness2=3.1660530633967037
    //    val thickness3=4.1409008240245395
    //    val thickness4=3.168285353589632


    //    val alphaShape = 84.60929507839191
    //    val angle0 = 109.33351380436731
    //    val angle1 = 26.32780867352028
    //    val angle2 = 180.0
    //    val angle3 = 37.529460017002116
    //    val angle4 = 132.3826589108104
    //    val decreaseRate0 = 1.0484818689356694
    //    val decreaseRate1 = 1.3299820030991392
    //    val decreaseRate2 = 1.2992188035820753
    //    val decreaseRate3 = 0.9665520658838445
    //    val decreaseRate4 = 1.0457225082401134
    //    val nbBifurcation0 = 4.812604040043527
    //    val nbBifurcation1 = 4.621270507196047
    //    val nbBifurcation2 = 1.2899129494690027
    //    val nbBifurcation3 = 4.869888909839968
    //    val nbBifurcation4 = 5.484205330990891
    //    val seed = 5778688202802294371L
    //    val sterilityRate0 = 0.2811438972814025
    //    val sterilityRate1 = 0.12019320578751835
    //    val sterilityRate2 = 0.3875011727329625
    //    val sterilityRate3 = 0.8904818797021251
    //    val sterilityRate4 = 0.39628467877975215
    //    val thickness0 = 5.375965868328333
    //    val thickness1 = 7.1594824361863125
    //    val thickness2 = 6.447265656110175
    //    val thickness3 = 9.370836991539655
    //    val thickness4 = 6.621177576645073

    val alphaShape = 59.88869733562845
    val angle0 = 127.12375027708086
    val angle1 = 175.84647766744024
    val angle2 = 92.24860231496892
    val angle3 = 7.93964909643049
    val angle4 = 0.0
    val decreaseRate0 = 1.3784663295937998
    val decreaseRate1 = 1.280709527938833
    val decreaseRate2 = 1.1677051706047559
    val decreaseRate3 = 0.6550140181612569
    val decreaseRate4 = 1.3904091083541052
    val nbBifurcation0 = 4.455874491417808
    val nbBifurcation1 = 3.8159778494906513
    val nbBifurcation2 = 3.6249167012520065
    val nbBifurcation3 = 4.728181306890837
    val nbBifurcation4 = 5.674974421236466
    val openmole$seed = 5778688202802297382L
    val sterilityRate0 = 0.04567499685252552
    val sterilityRate1 = 0.26730457576614125
    val sterilityRate2 = 0.4990060874958304
    val sterilityRate3 = 0.17450903142832114
    val sterilityRate4 = 0.8956782745535835
    val thickness0 = 9.988047737267477
    val thickness1 = 2.287185515579269
    val thickness2 = 4.468115992206393
    val thickness3 = 1.0
    val thickness4 = 8.133256887092823
    Model(
      alphaShape,
      thickness0,
      decreaseRate0,
      angle0,
      nbBifurcation0.round.toInt,
      sterilityRate0,

      thickness1,
      decreaseRate1,
      angle1,
      nbBifurcation1.round.toInt,
      sterilityRate1,

      thickness2,
      decreaseRate2,
      angle2,
      nbBifurcation2.round.toInt,
      sterilityRate2,

      thickness3,
      decreaseRate3,
      angle3,
      nbBifurcation3.round.toInt,
      sterilityRate3,

      thickness4,
      decreaseRate4,
      angle4,
      nbBifurcation4.round.toInt,
      sterilityRate4
    )
  }
}
