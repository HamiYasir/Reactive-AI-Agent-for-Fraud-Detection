import os
from flask import Flask, request, jsonify
import joblib
import numpy as np
import pandas as pd
from flask_cors import CORS

# Load the trained XGBoost model
model = joblib.load("Model/XGBoost/xgboost_fraud_model.pkl")

app = Flask(__name__)
CORS(app)

# Define the correct feature list
feature_list = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 
                'V10', 'V11', 'V12', 'V13', 'V14', 'V15', 'V16', 'V17', 'V18', 
                'V19', 'V20', 'V21', 'V22', 'V23', 'V24', 'V25', 'V26', 'V27', 'V28', 'Amount']

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.json  
        if not data:
            return jsonify({"error": "Empty request data"}), 400

        df = pd.DataFrame(data if isinstance(data, list) else [data])

        missing_features = [feature for feature in feature_list if feature not in df.columns]
        if missing_features:
            return jsonify({"error": f"Missing features: {missing_features}"}), 400

        if 'id' in df.columns:
            df.pop('id')

        df = df[feature_list]
        features = df.to_numpy()

        predictions = model.predict(features)
        probabilities = model.predict_proba(features)[:, 1]

        response = [{"fraud": bool(pred), "probability": float(prob)}
                    for pred, prob in zip(predictions, probabilities)]

        return jsonify(response)

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(
        host="0.0.0.0",
        port=int(os.environ.get("PORT", 5000))
    )
