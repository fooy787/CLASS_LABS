using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class textBox : MonoBehaviour {
    string toDisplay = "";
    float sWidth = Screen.width / 8;
    float sHeight = Screen.height - (Screen.height / 4);
    float fWidth = Screen.width - ((Screen.width / 8) * 2);
    float fHeight = (Screen.height / 5);
    public void MakeTextBox(string text)
    {
        toDisplay = text;
    }
    void OnGUI()
    {
        GUI.Box(new Rect(sWidth, sHeight, fWidth, fHeight), toDisplay);
    }
}
