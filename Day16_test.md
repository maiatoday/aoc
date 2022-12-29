```mermaid
stateDiagram-v2
    AA_0 --> DD_20
    AA_0 --> II_0
    AA_0 --> BB_13
    BB_13 --> CC_2
    BB_13 --> AA_0
    CC_2 --> DD_20
    CC_2 --> BB_13
    DD_20 --> CC_2
    DD_20 --> AA_0
    DD_20 --> EE_3
    EE_3 --> FF_0
    EE_3 --> DD_20
    FF_0 --> EE_3
    FF_0 --> GG_0
    GG_0 --> FF_0
    GG_0 --> HH_22
    HH_22 --> GG_0
    II_0 --> AA_0
    II_0 --> JJ_21
    JJ_21 --> II_0
```