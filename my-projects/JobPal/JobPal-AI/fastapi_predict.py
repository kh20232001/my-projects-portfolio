# ライブラリのインポート
from fastapi import FastAPI
import pandas as pd
from pydantic import BaseModel
from joblib import load
from starlette.middleware.cors import CORSMiddleware

import spacy
from sklearn.ensemble import RandomForestClassifier

# 保存したモデルを読み込む
model = load('nega_posi.pkl')

# FastAPIのアプリケーションを作成
app = FastAPI()

# CORSを回避するために追加
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]
)

# 学習済みモデルとしてginzaをロード
nlp = spacy.load("ja_ginza")

class JobDetailRequest(BaseModel):
    text: str

@app.post("/jobdetail")
def negapoji(request: JobDetailRequest):
    text = request.text
    doc = nlp(text)
    emotions = model.predict([doc.vector])[0]
    if emotions == "ポジティブ":
        return "合格"
    else:
        return "不合格"

