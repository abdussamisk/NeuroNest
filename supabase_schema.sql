-- ====================================================================
-- NeuroNest Supabase Database Schema
-- Run this script in the Supabase Dashboard SQL Editor
-- ====================================================================

-- 1. Patient Profiles Table
CREATE TABLE IF NOT EXISTS public.patient_profiles (
    id TEXT PRIMARY KEY DEFAULT 'default_patient',
    name TEXT NOT NULL DEFAULT 'Patient User',
    age INT NOT NULL DEFAULT 72,
    selected_region TEXT NOT NULL DEFAULT 'ASSAM',
    preferred_language TEXT NOT NULL DEFAULT 'Assamese',
    current_difficulty_level INT NOT NULL DEFAULT 2,
    cognitive_health_index FLOAT NOT NULL DEFAULT 84.5,
    baseline_score FLOAT NOT NULL DEFAULT 82.0,
    caregiver_contact_phone TEXT NOT NULL DEFAULT '+91 98765 43210',
    voice_prompts_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 2. Cognitive Game Sessions Table
CREATE TABLE IF NOT EXISTS public.cognitive_sessions (
    id TEXT PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    game_type TEXT NOT NULL,
    difficulty_level INT NOT NULL,
    score INT NOT NULL,
    total_attempts INT NOT NULL,
    successful_attempts INT NOT NULL,
    average_reaction_time_ms BIGINT NOT NULL,
    accuracy_percentage FLOAT NOT NULL,
    is_baseline_dip_detected BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 3. Caregiver Alerts Table
CREATE TABLE IF NOT EXISTS public.caregiver_alerts (
    id TEXT PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    alert_type TEXT NOT NULL,
    severity TEXT NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    is_dismissed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Indexing for high performance queries
CREATE INDEX IF NOT EXISTS idx_cognitive_sessions_timestamp ON public.cognitive_sessions(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_cognitive_sessions_game_type ON public.cognitive_sessions(game_type);
CREATE INDEX IF NOT EXISTS idx_caregiver_alerts_timestamp ON public.caregiver_alerts(timestamp DESC);

-- Enable Row Level Security (RLS)
ALTER TABLE public.patient_profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.cognitive_sessions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.caregiver_alerts ENABLE ROW LEVEL SECURITY;

-- Allow anonymous & authenticated access (for initial dev / test setup)
CREATE POLICY "Allow public read/write access to patient_profiles"
    ON public.patient_profiles FOR ALL USING (true) WITH CHECK (true);

CREATE POLICY "Allow public read/write access to cognitive_sessions"
    ON public.cognitive_sessions FOR ALL USING (true) WITH CHECK (true);

CREATE POLICY "Allow public read/write access to caregiver_alerts"
    ON public.caregiver_alerts FOR ALL USING (true) WITH CHECK (true);
