using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class textBox : MonoBehaviour
{
    string toDisplay = "";
    float sWidth = Screen.width / 8;
    float sHeight = Screen.height - (Screen.height / 4);
    float fWidth = Screen.width - ((Screen.width / 8) * 2);
    float fHeight = (Screen.height / 5);
    bool showBox = false;
    public void changeText(string text)
    {
        toDisplay = text;
    }
    public void switchOn()
    {
        showBox = true;
    }
    public void switchOff()
    {
        showBox = false;
    }

    void OnGUI()
    {
        //Debug.Log(showBox);
        if (showBox == true)
        {
            GUI.Box(new Rect(sWidth, sHeight, fWidth, fHeight), toDisplay);
        }
    }
}