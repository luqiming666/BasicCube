using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SimpleLogic : MonoBehaviour
{
    [Header("旋转速度"), Range(10, 100)]
    public float rotateSpeed = 50f;

    // Start is called before the first frame update
    void Start()
    {
        //transform.localEulerAngles = new Vector3(90, 0, 0);
    }

    // Update is called once per frame
    void Update()
    {
        Vector3 angels = transform.localEulerAngles;
        angels.y += rotateSpeed * Time.deltaTime;
        transform.localEulerAngles = angels;
    }

    // UI布局：https://blog.csdn.net/qq_43511290/article/details/95752308
    private void OnGUI()
    {
        GUILayout.BeginArea(new Rect(200, 200, 500, 200));//固定布局  //Rect(float x,float y,float width,float height)  
        GUILayout.BeginHorizontal();//内层嵌套一个横向布局 

        GUIStyle style = new GUIStyle(GUI.skin.button);
        style.border = new RectOffset(5, 5, 5, 5);
        style.normal.textColor = Color.white;
        style.fontSize = 32;

        if (GUILayout.Button("Speed Up", style, GUILayout.Width(200), GUILayout.Height(200)))
        {
            rotateSpeed += 20f;
        }

        if (GUILayout.Button("Speed Down", style, GUILayout.Width(200), GUILayout.Height(200)))
        {
            if (rotateSpeed > 10f)
            {
                rotateSpeed -= 10f;
            }
        }

        GUILayout.EndHorizontal();
        GUILayout.EndArea();
    }
}
