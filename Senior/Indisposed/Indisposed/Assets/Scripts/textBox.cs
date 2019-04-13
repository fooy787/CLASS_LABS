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

    /// <summary>
    /// Call this to change what is currently stored in the text box to display.
    /// </summary>
    /// <param name="text">This is what you want the text box to change it's text too.</param>
    public void changeText(string text)
    {
        toDisplay = text;
    }

    /// <summary>
    /// This is called to turn on the textbox.
    /// </summary>
    public void switchOn()
    {
            showBox = true;
    }

    /// <summary>
    /// This is called to turn off the textbox.
    /// </summary>
    public void switchOff()
    {
        showBox = false;
    }

    /// <summary>
    /// This gets what text is currently held by the text box to display, without displaying it or changing it.
    /// </summary>
    /// <returns>returns what textbox is ready to display.</returns>
    public string getDisplay()
    {
        return toDisplay;
    }

    /// <summary>
    /// This is called every frame to see if the text box should be displayed.
    /// </summary>
    void OnGUI()
    {
        if (showBox == true)
        {
            GUI.Box(new Rect(sWidth, sHeight, fWidth, fHeight), toDisplay);
        }
    }
}


