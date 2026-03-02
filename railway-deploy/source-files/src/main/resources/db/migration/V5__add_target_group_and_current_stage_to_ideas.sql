-- Add target_group and current_stage columns to ideas table

-- Add target_group column if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'ideas'
        AND column_name = 'target_group'
    ) THEN
        ALTER TABLE ideas ADD COLUMN target_group TEXT;
    END IF;
END $$;

-- Add current_stage column if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'ideas'
        AND column_name = 'current_stage'
    ) THEN
        ALTER TABLE ideas ADD COLUMN current_stage VARCHAR(255);
    END IF;
END $$;
