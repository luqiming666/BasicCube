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
        GUILayout.BeginArea(new Rect(600, 300, 200, 80));//固定布局  //Rect(float x,float y,float width,float height)  
        GUILayout.BeginHorizontal();//内层嵌套一个横向布局 

        if (GUILayout.Button("Speed Up"))
        {
            rotateSpeed += 20f;
        }

        if (GUILayout.Button("Speed Down"))
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
